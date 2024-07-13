package me.heyner.inventorypro.service;

import java.util.List;
import me.heyner.inventorypro.exception.OptionNotFoundException;
import me.heyner.inventorypro.exception.ProductNotFoundException;
import me.heyner.inventorypro.model.Option;
import me.heyner.inventorypro.model.OptionValue;
import me.heyner.inventorypro.model.Product;
import me.heyner.inventorypro.repository.OptionRepository;
import me.heyner.inventorypro.repository.OptionValuesRepository;
import me.heyner.inventorypro.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

  private static final Logger logger = LoggerFactory.getLogger(OptionService.class);

  private final OptionRepository optionRepository;

  private final OptionValuesRepository optionValuesRepository;

  private final ProductService productService;

  public OptionService(
      OptionRepository optionRepository,
      ProductService productService,
      ProductRepository productRepository,
      OptionValuesRepository optionValuesRepository) {
    this.optionRepository = optionRepository;
    this.productService = productService;
    this.optionValuesRepository = optionValuesRepository;
  }

  public Option addOption(Long productId, Option option) throws ProductNotFoundException {
    Product product = productService.findById(productId);
    optionRepository.save(option);
    System.out.println("test" + 12);
    logger.info("New option {} added to product {}", option.getName(), product.getName());
    return option;
  }

  public Option getOption(Long productId, int index) throws OptionNotFoundException {
    try {
      Option option = optionRepository.findByProduct_Id(productId).get(index);
      logger.info("Getting option number {} of product with the id {}", index, productId);

      return option;
    } catch (IndexOutOfBoundsException ex) {
      throw new OptionNotFoundException((long) index);
    }
  }

  public List<Option> getOptionsByProductId(Long productId) throws ProductNotFoundException {
    List<Option> options = optionRepository.findByProduct_Id(productId);
    logger.info("Getting {} options, for product {}", options.size(), productId);
    return options;
  }

  public Option updateOption(Long productId, Option option) throws OptionNotFoundException {
    Option optionToUpdate = getOption(productId, option.getIndex());
    option.setId(optionToUpdate.getId());
    optionRepository.save(option);
    logger.info(
        "Option {} of product {} updated to {}",
        optionToUpdate.getName(),
        productId,
        option.getName());

    return option;
  }

  public void removeOption(Long productId, int index)
      throws ProductNotFoundException, OptionNotFoundException {
    Option optionToRemove = getOption(productId, index);

    optionRepository.delete(optionToRemove);
    logger.info(
        "Option {} of product {} successfully deleted",
        optionToRemove.getName(),
        optionToRemove.getProduct().getName());
  }

  public OptionValue addValue(Long productId, int optionIndex, OptionValue optionValue) {
    Option option = getOption(productId, optionIndex);
    optionValue.setOption(option);
    optionValuesRepository.save(optionValue);
    logger.info(
        "Option value {} successfully added to option {}",
        optionValue.getValue(),
        option.getName());
    return optionValue;
  }

  public OptionValue getOptionValue(Long productId, int optionIndex, int optionValueIndex) {
    OptionValue optionValue = getOption(productId, optionIndex).getValues().get(optionValueIndex);
    logger.info(
        "Getting value {} for option index {} and product id {}",
        optionValue.getValue(),
        optionIndex,
        productId);

    return optionValue;
  }

  public OptionValue updateOptionValue(Long productId, int optionIndex, OptionValue optionValue) {
    OptionValue optionValueToUpdate =
        getOptionValue(productId, optionIndex, optionValue.getIndex());
    optionValue.setId(optionValueToUpdate.getId());
    optionValuesRepository.save(optionValue);
    logger.info(
        "Value {} for option index {} and product id {} successfully updated",
        optionValue.getValue(),
        optionIndex,
        productId);
    return optionValue;
  }

  public void removeOptionValue(Long productId, int optionIndex, int optionValueIndex) {
    OptionValue optionValue = getOptionValue(productId, optionIndex, optionValueIndex);
    optionValuesRepository.delete(optionValue);
    logger.info(
        "Value {} for option index {} and product id {} successfully removed",
        optionValue.getValue(),
        optionIndex,
        productId);
  }
}
