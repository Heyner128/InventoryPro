package me.heyner.stashless.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import me.heyner.stashless.dto.SKUInputDto;
import me.heyner.stashless.dto.SKUOutputDto;
import me.heyner.stashless.exception.EntityNotFoundException;
import me.heyner.stashless.model.*;
import me.heyner.stashless.repository.OptionRepository;
import me.heyner.stashless.repository.ProductRepository;
import me.heyner.stashless.repository.SKURepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SKUService {

  private final SKURepository skuRepository;
  private final ProductRepository productRepository;
  private final OptionRepository optionRepository;
  Logger logger = LoggerFactory.getLogger(SKUService.class);
  ModelMapper modelMapper = new ModelMapper();

  public SKUService(
      SKURepository skuRepository,
      OptionRepository optionRepository,
      ProductRepository productRepository) {
    this.skuRepository = skuRepository;
    this.optionRepository = optionRepository;
    this.productRepository = productRepository;
  }

  public SKUOutputDto addSKU(UUID productUuid, SKUInputDto skuDto) throws EntityNotFoundException {
    Product product =
        productRepository
            .findById(productUuid)
            .orElseThrow(() -> new EntityNotFoundException("Not found"));

    Option option =
        optionRepository
            .findById(skuDto.getOptionUUID())
            .orElseThrow(() -> new EntityNotFoundException("Not found"));

    OptionValue optionValue =
        option.getValues().stream()
            .filter(ov -> ov.getValue().equals(skuDto.getOptionValue()))
            .findFirst()
            .orElseThrow(() -> new EntityNotFoundException("Not found"));

    SKU sku = modelMapper.map(skuDto, SKU.class);
    sku.setProduct(product);
    sku.setOption(option);
    sku.setOptionValue(optionValue);
    SKU savedSku = skuRepository.save(sku);
    logger.info("SKU created: " + sku);
    return modelMapper.map(savedSku, SKUOutputDto.class);
  }

  public List<SKUOutputDto> getSkus(UUID productUuid) throws EntityNotFoundException {
    List<SKU> skus = skuRepository.findByProduct_Id(productUuid);
    logger.info("SKUs {} found", skus.size());
    return skus.stream().map(sku -> modelMapper.map(sku, SKUOutputDto.class)).toList();
  }

  public List<SKUOutputDto> updateSkus(UUID productUUID, List<SKUInputDto> skuDtos)
      throws EntityNotFoundException {
    Product product =
        productRepository
            .findById(productUUID)
            .orElseThrow(() -> new EntityNotFoundException("Not found"))
            .setSkus(
                skuDtos.stream()
                    .map(skuDto -> new SKU().setName(skuDto.getName()))
                    .collect(Collectors.toList()));

    productRepository.save(product);

    return product.getSkus().stream().map(sku -> modelMapper.map(sku, SKUOutputDto.class)).toList();
  }
}
