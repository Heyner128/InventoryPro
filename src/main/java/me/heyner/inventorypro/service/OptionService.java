package me.heyner.inventorypro.service;

import java.util.List;
import java.util.UUID;
import me.heyner.inventorypro.dto.OptionDto;
import me.heyner.inventorypro.exception.EntityNotFoundException;
import me.heyner.inventorypro.model.Option;
import me.heyner.inventorypro.model.Product;
import me.heyner.inventorypro.repository.OptionRepository;
import me.heyner.inventorypro.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

  private static final Logger logger = LoggerFactory.getLogger(OptionService.class);

  private final ProductRepository productRepository;

  private final OptionRepository optionRepository;

  private final ModelMapper modelMapper = new ModelMapper();

  public OptionService(ProductRepository productRepository, OptionRepository optionRepository) {
    this.productRepository = productRepository;
    this.optionRepository = optionRepository;
  }

  public OptionDto addOption(UUID productUuid, OptionDto optionDto) throws EntityNotFoundException {
    Product product =
        productRepository
            .findById(productUuid)
            .orElseThrow(() -> new EntityNotFoundException("Not found"));
    Option option = modelMapper.map(optionDto, Option.class);
    option.setProduct(product);
    Option savedOption = optionRepository.save(option);
    logger.info("Saved option: {}", savedOption);
    return modelMapper.map(savedOption, OptionDto.class);
  }

  public List<OptionDto> getOptions(UUID productUuid) throws EntityNotFoundException {
    List<Option> options = optionRepository.findByProduct_Id(productUuid);
    logger.info("Getting {} options for product {}", options.size(), productUuid);
    return options.stream().map(opt -> modelMapper.map(opt, OptionDto.class)).toList();
  }

  public List<OptionDto> updateOptions(UUID productUuid, List<OptionDto> optionDtos)
      throws EntityNotFoundException {

    Product product =
        productRepository
            .findById(productUuid)
            .orElseThrow(() -> new EntityNotFoundException("Not found"));
    product.setOptions(
        optionDtos.stream()
            .map(optDto -> new Option().setProduct(product).setName(optDto.getName()))
            .toList());
    productRepository.save(product);
    return optionRepository.findByProduct_Id(productUuid).stream()
        .map(opt -> modelMapper.map(opt, OptionDto.class))
        .toList();
  }
}
