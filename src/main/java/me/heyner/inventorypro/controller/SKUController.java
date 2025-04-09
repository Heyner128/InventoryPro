package me.heyner.inventorypro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import me.heyner.inventorypro.dto.SKUInputDto;
import me.heyner.inventorypro.dto.SKUOutputDto;
import me.heyner.inventorypro.model.SKU;
import me.heyner.inventorypro.service.SKUService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "JWT token")
@Tag(name = "SKU")
@RestController
@RequestMapping("/users/{username}/products/{productUuid}/skus")
public class SKUController {

  private final SKUService skuService;

  public SKUController(SKUService skuService) {
    this.skuService = skuService;
  }

  @GetMapping
  public List<SKUOutputDto> getProductSKUs(@PathVariable UUID productUuid) {
    return skuService.getSkus(productUuid);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public SKUOutputDto createSKU(@PathVariable UUID productUuid, @Valid @RequestBody SKUInputDto skuDto) {
    return skuService.addSKU(productUuid, skuDto);
  }

  @PutMapping
  public List<SKUOutputDto> updateSKUs(
      @PathVariable UUID productUuid, @RequestBody List<SKUInputDto> skuDtos) {
    return skuService.updateSkus(productUuid, skuDtos);
  }
}
