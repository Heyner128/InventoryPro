package me.heyner.inventorypro.service;

import java.util.List;
import me.heyner.inventorypro.dto.OptionDto;
import me.heyner.inventorypro.exception.OptionNotFoundException;
import me.heyner.inventorypro.exception.ProductNotFoundException;
import me.heyner.inventorypro.exception.UserNotFoundException;
import me.heyner.inventorypro.model.Option;
import me.heyner.inventorypro.model.Product;
import me.heyner.inventorypro.repository.OptionRepository;
import me.heyner.inventorypro.repository.OptionValuesRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

  private static final Logger logger = LoggerFactory.getLogger(OptionService.class);

  private final OptionRepository optionRepository;

  private final OptionValuesRepository optionValuesRepository;

  private final ProductService productService;

  private final ModelMapper modelMapper = new ModelMapper();

  public OptionService(
      OptionRepository optionRepository,
      ProductService productService,
      OptionValuesRepository optionValuesRepository) {
    this.optionRepository = optionRepository;
    this.productService = productService;
    this.optionValuesRepository = optionValuesRepository;
  }

  private Option createOption(String username, int productIndex, OptionDto optionDto)
      throws ProductNotFoundException, UserNotFoundException {
    Product product = productService.getProduct(username, productIndex);
    Option option = modelMapper.map(optionDto, Option.class);
    option.setProduct(product);
    return option;
  }

  public Option addOption(String username, int productIndex, OptionDto optionDto)
      throws ProductNotFoundException, UserNotFoundException {
    Option option = createOption(username, productIndex, optionDto);
    logger.info(
        "New option {} added to product {} of the user {}",
        option.getName(),
        productIndex,
        username);
    return option;
  }

  public Option getOption(String username, int productIndex, int optionIndex)
      throws ProductNotFoundException, OptionNotFoundException {
    try {
      Product product = productService.getProductWithOptions(username, productIndex);
      Option option = product.getOptions().get(optionIndex);
      logger.info("Getting option number {} of product {}", optionIndex, productIndex);
      return option;
    } catch (IndexOutOfBoundsException ex) {
      throw new OptionNotFoundException("Not found");
    }
  }

  public List<Option> getOptions(String username, int productIndex)
      throws ProductNotFoundException {
    Product product = productService.getProductWithOptions(username, productIndex);
    List<Option> options = product.getOptions();
    logger.info("Getting {} options for product {}", options.size(), product.getId());
    return options;
  }

  public Option updateOption(
      String username, int productIndex, int optionIndex, OptionDto optionDto)
      throws OptionNotFoundException {
    Option optionToUpdate = getOption(username, productIndex, optionIndex);
    optionToUpdate.setName(optionDto.getName());
    optionRepository.save(optionToUpdate);
    logger.info("Option {} updated to {}", optionToUpdate.getName(), optionDto.getName());

    return optionToUpdate;
  }

  public void removeOption(String username, int productIndex, int optionIndex)
      throws ProductNotFoundException, OptionNotFoundException {
    Option optionToRemove = getOption(username, productIndex, optionIndex);
    optionRepository.delete(optionToRemove);
    logger.info(
        "Option {} of product {} successfully deleted",
        optionToRemove.getName(),
        optionToRemove.getProduct().getName());
  }
}
