package me.heyner.inventorypro.controller;

import jakarta.validation.Valid;
import me.heyner.inventorypro.exception.ConflictingIndexesException;
import me.heyner.inventorypro.model.SKUValue;
import me.heyner.inventorypro.service.SKUService;
import me.heyner.inventorypro.model.SKU;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SKUController {

    private final SKUService skuService;

    public SKUController(SKUService skuService) {
        this.skuService = skuService;
    }


    @GetMapping("/products/{productId}/skus")
    public List<SKU> getProductSKUs(@PathVariable Long productId) {
        return skuService.getSkusByProductId(productId);
    }

    @PostMapping("/products/{productId}/skus")
    public SKU createSKU(@PathVariable Long productId, @RequestBody @Valid SKU sku) {
        return skuService.addSKU(productId, sku);
    }

    @GetMapping("/products/{productId}/skus/{skuIndex}")
    public SKU getSKU(@PathVariable Long productId, @PathVariable int skuIndex) {
        return skuService.getSKU(productId, skuIndex);
    }

    @PutMapping("/products/{productId}/skus/{skuIndex}")
    public SKU updateSKU(@PathVariable Long productId, @PathVariable int skuIndex, @RequestBody @Valid SKU sku) {
        if(skuIndex != sku.getIndex()) throw  new ConflictingIndexesException();
        return skuService.updateSKU(productId, sku);
    }

    @DeleteMapping("/products/{productId}/skus/{skuIndex}")
    public void deleteSKU(@PathVariable Long productId, @PathVariable int skuIndex) {
        skuService.removeSKU(productId, skuIndex);
    }

    @GetMapping("/products/{productId}/skus/{skuIndex}/values")
    public List<SKUValue> getSKUValues(@PathVariable Long productId, @PathVariable int skuIndex) {
        return skuService.getSKU(productId, skuIndex).getValues();
    }

    @PostMapping("/products/{productId}/skus/{skuIndex}/values")
    public SKUValue addSKUValue(@PathVariable Long productId, @PathVariable int skuIndex, @RequestBody @Valid SKUValue skuValue) {
        return skuService.addValue(productId, skuIndex, skuValue);
    }

    @GetMapping("/products/{productId}/skus/{skuIndex}/values/{skuValueIndex}")
    public SKUValue getSKUValue(@PathVariable Long productId, @PathVariable int skuIndex, @PathVariable int skuValueIndex) {
        return skuService.getSKUValue(productId, skuIndex, skuValueIndex);
    }

    @PutMapping("/products/{productId}/skus/{skuIndex}/values/{skuValueIndex}")
    public SKUValue updateSKUValue(@PathVariable Long productId, @PathVariable int skuIndex,@PathVariable int skuValueIndex, @RequestBody @Valid SKUValue skuValue) {
        if(skuValueIndex != skuValue.getIndex()) throw new ConflictingIndexesException();
        return skuService.updateSKUValue(productId, skuIndex, skuValue);
    }

    @DeleteMapping("/products/{productId}/skus/{skuIndex}/values/{skuValueIndex}")
    public void deleteSKUValue(@PathVariable Long productId, @PathVariable int skuIndex, @PathVariable int skuValueIndex) {
        skuService.removeSKUValue(productId, skuIndex, skuValueIndex);
    }



}
