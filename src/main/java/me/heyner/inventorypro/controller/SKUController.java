package me.heyner.inventorypro.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import me.heyner.inventorypro.dto.SKUDto;
import me.heyner.inventorypro.service.SKUService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{username}/products/{productUuid}/skus")
public class SKUController {

  private final SKUService skuService;

  public SKUController(SKUService skuService) {
    this.skuService = skuService;
  }

  @GetMapping
  public List<SKUDto> getProductSKUs(@PathVariable UUID productUuid) {
    return skuService.getSkus(productUuid);
  }

  @PostMapping
  public SKUDto createSKU(@PathVariable UUID productUuid, @Valid @RequestBody SKUDto skuDto) {
    return skuService.addSKU(productUuid, skuDto);
  }

  @PutMapping
  public List<SKUDto> updateSKUs(
      @PathVariable UUID productUuid, @RequestBody List<SKUDto> skuDtos) {
    return skuService.updateSkus(productUuid, skuDtos);
  }
}
