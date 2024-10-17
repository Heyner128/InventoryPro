package me.heyner.inventorypro.service;

import java.util.List;
import java.util.UUID;
import me.heyner.inventorypro.dto.OptionInputDto;
import me.heyner.inventorypro.exception.EntityNotFoundException;
import me.heyner.inventorypro.model.Option;
import me.heyner.inventorypro.model.OptionValue;
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

  public Option addOption(UUID productUuid, OptionInputDto optionDto)
      throws EntityNotFoundException {
    Product product =
        productRepository
            .findById(productUuid)
            .orElseThrow(() -> new EntityNotFoundException("Not found"));
    Option option =
        new Option()
            .setName(optionDto.getName())
            .setProduct(product)
            .setValues(
                optionDto.getValues().stream()
                    .map(ovs -> new OptionValue().setValue(ovs))
                    .toList());

    Option savedOption = optionRepository.save(option);
    logger.info("Saved option: {}", savedOption);
    return savedOption;
  }

  public List<Option> getOptions(UUID productUuid) throws EntityNotFoundException {
    List<Option> options = optionRepository.findByProduct_Id(productUuid);
    logger.info("Getting {} options for product {}", options.size(), productUuid);
    return options;
  }

  public List<Option> updateOptions(UUID productUuid, List<OptionInputDto> optionsDto)
      throws EntityNotFoundException {

    Product product =
        productRepository
            .findById(productUuid)
            .orElseThrow(() -> new EntityNotFoundException("Not found"))
            .setOptions(
                optionsDto.stream().map(optDto -> modelMapper.map(optDto, Option.class)).toList());
    productRepository.save(product);
    return optionRepository.findByProduct_Id(productUuid);
  }
}
