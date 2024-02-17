package me.heyner.inventorypro.service;

import me.heyner.inventorypro.exception.ProductNotFoundException;
import me.heyner.inventorypro.exception.SKUNotFoundException;
import me.heyner.inventorypro.model.Product;
import me.heyner.inventorypro.model.SKU;
import me.heyner.inventorypro.repository.SKURepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SKUService {

    Logger logger = LoggerFactory.getLogger(SKUService.class);

    private final SKURepository skuRepository;

    private final ProductService productService;

    public SKUService(SKURepository skuRepository, ProductService productService) {
        this.skuRepository = skuRepository;
        this.productService = productService;
    }

    public SKU addSKU(Long productId, SKU sku) throws ProductNotFoundException {
        Product product  = productService.findById(productId);
        skuRepository.save(sku);
        logger.info("New sku " + sku.getSku() + " added to product " + product.getName());
        return sku;
    }

    public Set<SKU> getByProductId(Long productId) throws ProductNotFoundException {
        Product product = productService.findById(productId);
        Set<SKU> skus = product.getSkus();
        logger.info("Getting " + skus.size() + " skus, for product " + product.getName());
        return skus;
    }

    public SKU updateSKU(SKU sku) throws SKUNotFoundException {
        SKU skuToUpdate = skuRepository.findById(sku.getId())
                .orElseThrow(() -> new SKUNotFoundException("SKU not found"));
        skuRepository.save(sku);
        logger.info(
            "SKU " +
            skuToUpdate.getSku() +
            " of product " +
            skuToUpdate.getProduct().getName() +
            " updated to " +
            sku.getSku()
        );

        return sku;
    }

    public void removeOption(Long productId, String name) throws ProductNotFoundException, SKUNotFoundException {
        Product product = productService.findById(productId);
        SKU sku = skuRepository.findBySkuIgnoreCaseAndProduct(name, product)
                .orElseThrow(() -> new SKUNotFoundException("sku not found"));

        skuRepository.delete(sku);

        logger.info("SKU" +
                sku.getSku() +
                " of product " +
                product.getName() +
                " successfully deleted "
            );

    }
}
