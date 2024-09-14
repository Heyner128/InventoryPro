package me.heyner.inventorypro.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import me.heyner.inventorypro.dto.SKUDto;
import me.heyner.inventorypro.exception.EntityNotFoundException;
import me.heyner.inventorypro.model.*;
import me.heyner.inventorypro.repository.ProductRepository;
import me.heyner.inventorypro.repository.SKURepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SKUService {

  private final SKURepository skuRepository;
  private final ProductRepository productRepository;
  Logger logger = LoggerFactory.getLogger(SKUService.class);
  ModelMapper modelMapper = new ModelMapper();

  public SKUService(
      SKURepository skuRepository,
      ProductService productService,
      ProductRepository productRepository) {
    this.skuRepository = skuRepository;
    this.productRepository = productRepository;
  }

  public SKUDto addSKU(UUID productUuid, SKUDto skuDto) throws EntityNotFoundException {
    Product product =
        productRepository
            .findById(productUuid)
            .orElseThrow(() -> new EntityNotFoundException("Not found"));
    SKU sku = modelMapper.map(skuDto, SKU.class);
    sku.setProduct(product);
    SKU savedSku = skuRepository.save(sku);
    logger.info("SKU created: " + sku);
    return modelMapper.map(savedSku, SKUDto.class);
  }

  public List<SKUDto> getSkus(UUID productUuid) throws EntityNotFoundException {
    List<SKU> skus = skuRepository.findByProduct_Id(productUuid);
    logger.info("SKUs {} found", skus.size());
    return skus.stream().map((element) -> modelMapper.map(element, SKUDto.class)).toList();
  }

  public List<SKUDto> updateSkus(UUID productUUID, List<SKUDto> skuDtos)
      throws EntityNotFoundException {
    Product product =
        productRepository
            .findById(productUUID)
            .orElseThrow(() -> new EntityNotFoundException("Not found"));
    product.setSkus(
        skuDtos.stream()
            .map(skuDto -> new SKU().setProduct(product).setSku(skuDto.getSku()))
            .collect(Collectors.toList()));

    productRepository.save(product);

    return skuRepository.findByProduct_Id(productUUID).stream()
        .map((element) -> modelMapper.map(element, SKUDto.class))
        .toList();
  }
}
