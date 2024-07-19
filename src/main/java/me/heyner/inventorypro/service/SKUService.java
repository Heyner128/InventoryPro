package me.heyner.inventorypro.service;

import java.util.List;
import me.heyner.inventorypro.dto.SKUDto;
import me.heyner.inventorypro.dto.SKUValueDto;
import me.heyner.inventorypro.exception.OptionNotFoundException;
import me.heyner.inventorypro.exception.ProductNotFoundException;
import me.heyner.inventorypro.exception.SKUNotFoundException;
import me.heyner.inventorypro.exception.UserNotFoundException;
import me.heyner.inventorypro.model.*;
import me.heyner.inventorypro.repository.SKURepository;
import me.heyner.inventorypro.repository.SKUValuesRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SKUService {

  private final SKURepository skuRepository;
  private final SKUValuesRepository skuValuesRepository;
  private final ProductService productService;
  private final OptionService optionService;
  Logger logger = LoggerFactory.getLogger(SKUService.class);
  ModelMapper modelMapper = new ModelMapper();

  public SKUService(
      SKURepository skuRepository,
      SKUValuesRepository skuValuesRepository,
      ProductService productService,
      OptionService optionService) {
    this.skuRepository = skuRepository;
    this.skuValuesRepository = skuValuesRepository;
    this.productService = productService;
    this.optionService = optionService;
  }

  private SKU createSKU(String username, int productIndex, SKUDto skuDto)
      throws ProductNotFoundException, UserNotFoundException {
    Product product = productService.getProduct(username, productIndex);
    SKU sku = modelMapper.map(skuDto, SKU.class);
    sku.setProduct(product);
    return sku;
  }

  public SKU addSKU(String username, int productIndex, SKUDto skuDto)
      throws ProductNotFoundException, UserNotFoundException {
    SKU sku = createSKU(username, productIndex, skuDto);
    skuRepository.save(sku);
    logger.info("New sku {} added", sku.getSku());
    return sku;
  }

  public SKU getSKU(String username, int productIndex, int skuIndex) throws SKUNotFoundException {
    try {
      Product product = productService.getProductWithSKU(username, productIndex);
      SKU sku = product.getSkus().get(skuIndex);
      logger.info("SKU {} found", sku.getSku());
      return sku;
    } catch (IndexOutOfBoundsException ex) {
      throw new SKUNotFoundException("SKU not found");
    }
  }

  public List<SKU> getSkus(String username, int productIndex) throws ProductNotFoundException {
    Product product = productService.getProduct(username, productIndex);
    List<SKU> skus = product.getSkus();
    logger.info("SKUs {} found", skus.size());
    return skus;
  }

  public SKU updateSKU(String username, int productIndex, int skuIndex, SKUDto skuDto)
      throws SKUNotFoundException {
    SKU skuToUpdate = getSKU(username, productIndex, skuIndex);
    skuToUpdate.setSku(skuDto.getSku());
    skuRepository.save(skuToUpdate);
    logger.info("SKU {} updated", skuToUpdate.getSku());
    return skuToUpdate;
  }

  public void removeSKU(String username, int productIndex, int skuIndex)
      throws SKUNotFoundException, ProductNotFoundException {
    SKU skuToRemove = getSKU(username, productIndex, skuIndex);
    skuRepository.delete(skuToRemove);
    logger.info("SKU {} removed", skuToRemove.getSku());
  }

  public SKUValue addValue(
      String username, int productIndex, int optionIndex, int skuIndex, SKUValueDto skuValueDto)
      throws ProductNotFoundException,
          UserNotFoundException,
          OptionNotFoundException,
          SKUNotFoundException {
    Option option = optionService.getOption(username, productIndex, optionIndex);
    SKU sku = getSKU(username, productIndex, skuIndex);
    SKUValue skuValue = modelMapper.map(skuValueDto, SKUValue.class);
    skuValue.setOption(option);
    skuValue.setSku(sku);
    skuValuesRepository.save(skuValue);
    logger.info("SKU value {} added", skuValue.getSku());
    return skuValue;
  }

  public void removeSKUValue(String username, int productIndex, int skuIndex, int skuValueIndex)
      throws ProductNotFoundException, OptionNotFoundException, SKUNotFoundException {
    try {
      SKU sku = getSKU(username, productIndex, skuIndex);
      SKUValue skuValue = sku.getValues().get(skuValueIndex);
      skuValuesRepository.delete(skuValue);
      logger.info("Value {} for sku index {} removed", skuValue.getSku(), skuValueIndex);
    } catch (IndexOutOfBoundsException ex) {
      logger.error(ex.getMessage(), ex);
      throw new SKUNotFoundException("SKU not found");
    }
  }
}
