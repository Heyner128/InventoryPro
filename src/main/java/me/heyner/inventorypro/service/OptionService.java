package me.heyner.inventorypro.service;

import me.heyner.inventorypro.exception.OptionNotFoundException;
import me.heyner.inventorypro.exception.ProductNotFoundException;
import me.heyner.inventorypro.model.Option;
import me.heyner.inventorypro.model.Product;
import me.heyner.inventorypro.repository.OptionRepository;
import me.heyner.inventorypro.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OptionService {

    private static final Logger logger = LoggerFactory.getLogger(OptionService.class);

    private final OptionRepository optionRepository;

    private final ProductService productService;

    public OptionService(OptionRepository optionRepository, ProductService productService,
                         ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productService = productService;
    }

    public Option addOption(Long productId, Option option) throws ProductNotFoundException {
        Product product = productService.findById(productId);
        optionRepository.save(option);
        logger.info("New option " + option.getName() + " added to product " + product.getName());
        return option;
    }

    public Set<Option> getByProductId(Long productId) throws ProductNotFoundException {
        Product product = productService.findById(productId);
        Set<Option> options = product.getOptions();
        logger.info("Getting " + options.size() + " options, for product " + product.getName());
        return options;
    }

    public Option updateOption (Option option) throws OptionNotFoundException {
        Option optionToUpdate = optionRepository.findById(option.getId())
                .orElseThrow(() -> new OptionNotFoundException("test"));

        optionRepository.save(option);
        logger.info("Option " +
                optionToUpdate.getName() +
                " of product " +
                optionToUpdate.getProduct().getName() +
                " updated to " +
                option.getName()
            );

        return option;
    }

    public void removeOption(Long productId, String name) throws ProductNotFoundException, OptionNotFoundException {
        Product product = productService.findById(productId);
        Option option = optionRepository.findByNameIgnoreCaseAndProduct(name, product)
                        .orElseThrow(() -> new OptionNotFoundException("Option not found"));

        optionRepository.delete(option);
        logger.info("Option " +
                option.getName() +
                " of product " +
                product.getName() +
                " successfully deleted"
            );
    }


}
