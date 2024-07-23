package me.heyner.inventorypro.controller;

import jakarta.validation.Valid;
import java.util.List;
import me.heyner.inventorypro.dto.SKUDto;
import me.heyner.inventorypro.model.SKU;
import me.heyner.inventorypro.service.SKUService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{username}/products/{productIndex}/skus")
public class SKUController {

  private final SKUService skuService;

  public SKUController(SKUService skuService) {
    this.skuService = skuService;
  }

  @GetMapping
  public List<SKU> getProductSKUs(@PathVariable int productIndex, @PathVariable String username) {
    return skuService.getSkus(username, productIndex);
  }

  @PostMapping
  public SKU createSKU(
      @PathVariable String username,
      @PathVariable int productIndex,
      @Valid @RequestBody SKUDto skuDto) {
    return skuService.addSKU(username, productIndex, skuDto);
  }

  @GetMapping("/{skuIndex}")
  public SKU getSKU(
      @PathVariable String username, @PathVariable int productIndex, @PathVariable int skuIndex) {
    return skuService.getSKU(username, productIndex, skuIndex);
  }

  @PutMapping("/{skuIndex}")
  public SKU updateSKU(
      @PathVariable String username,
      @PathVariable int productIndex,
      @PathVariable int skuIndex,
      @RequestBody SKUDto skuDto) {
    return skuService.updateSKU(username, productIndex, skuIndex, skuDto);
  }

  @DeleteMapping("/{skuIndex}")
  public void deleteSKU(
      @PathVariable String username, @PathVariable int productIndex, @PathVariable int skuIndex) {
    skuService.removeSKU(username, productIndex, skuIndex);
  }
}
