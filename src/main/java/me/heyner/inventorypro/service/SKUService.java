package me.heyner.inventorypro.service;

import me.heyner.inventorypro.exception.ProductNotFoundException;
import me.heyner.inventorypro.exception.SKUNotFoundException;
import me.heyner.inventorypro.model.*;
import me.heyner.inventorypro.repository.SKURepository;
import me.heyner.inventorypro.repository.SKUValuesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SKUService {

  Logger logger = LoggerFactory.getLogger(SKUService.class);

  private final SKURepository skuRepository;

  private final SKUValuesRepository skuValuesRepository;

  private final ProductService productService;

  public SKUService(
      SKURepository skuRepository,
      SKUValuesRepository skuValuesRepository,
      ProductService productService) {
    this.skuRepository = skuRepository;
    this.skuValuesRepository = skuValuesRepository;
    this.productService = productService;
  }

  public SKU addSKU(Long productId, SKU sku) throws ProductNotFoundException {
    Product product = productService.findById(productId);
    skuRepository.save(sku);
    logger.info("New sku {} added to product {}", sku.getSku(), product.getName());
    return sku;
  }

  public SKU getSKU(Long productId, int index) {
    Product product = productService.findById(productId);
    return product.getSkus().get(index);
  }

  public List<SKU> getSkusByProductId(Long productId) throws ProductNotFoundException {
    List<SKU> skus = skuRepository.findByProduct_Id(productId);
    logger.info("Getting {} skus, for product {}", skus.size(), productId);
    return skus;
  }

  public SKU updateSKU(Long productId, SKU sku) throws SKUNotFoundException {
    SKU skuToUpdate = getSKU(productId, sku.getIndex());
    sku.setId(skuToUpdate.getId());
    skuRepository.save(sku);
    logger.info("SKU {} of product {} updated", sku.getSku(), productId);
    return sku;
  }

  public void removeSKU(Long productId, int index)
      throws ProductNotFoundException, SKUNotFoundException {
    SKU skuToRemove = getSKU(productId, index);

    skuRepository.delete(skuToRemove);

    logger.info("SKU {} of product {}  successfully deleted ", skuToRemove.getSku(), productId);
  }

  public SKUValue addValue(Long productId, int skuIndex, SKUValue skuValue) {
    SKU sku = getSKU(productId, skuIndex);
    skuValue.setSku(sku);
    skuValuesRepository.save(skuValue);
    logger.info("SKU value {} successfully added to sku {}", skuValue.getSku(), sku.getSku());
    return skuValue;
  }

  public SKUValue getSKUValue(Long productId, int skuIndex, int skuValueIndex) {
    SKUValue skuValue = getSKU(productId, skuIndex).getValues().get(skuValueIndex);
    logger.info(
        "Getting value {} for sku index {} and product id {}",
        skuValue.getSku(),
        skuIndex,
        productId);

    return skuValue;
  }

  public SKUValue updateSKUValue(Long productId, int skuIndex, SKUValue skuValue) {
    SKUValue skuValueToUpdate = getSKUValue(productId, skuIndex, skuValue.getIndex());
    skuValue.setId(skuValueToUpdate.getId());
    skuValuesRepository.save(skuValue);
    logger.info(
        "Value {} for sku index {} and product id {} successfully updated",
        skuValue.getSku(),
        skuIndex,
        productId);
    return skuValue;
  }

  public void removeSKUValue(Long productId, int skuIndex, int skuValueIndex) {
    SKUValue skuValue = getSKUValue(productId, skuIndex, skuValueIndex);
    skuValuesRepository.delete(skuValue);
    logger.info(
        "Value {} for sku index {} and product id {} successfully removed",
        skuValue.getSku(),
        skuIndex,
        productId);
  }
}
