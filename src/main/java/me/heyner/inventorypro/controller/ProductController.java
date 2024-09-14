package me.heyner.inventorypro.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import me.heyner.inventorypro.dto.ProductDto;
import me.heyner.inventorypro.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{username}/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public List<ProductDto> getAllProducts(@PathVariable String username) {
    return productService.getProducts(username);
  }

  @GetMapping("/{productUuid}")
  public ProductDto getProduct(@PathVariable UUID uuid) {
    return productService.getProduct(uuid);
  }

  @PostMapping
  public ProductDto createProduct(
      @RequestBody @Valid ProductDto productDto, @PathVariable String username) {
    return productService.createProduct(username, productDto);
  }

  @PutMapping("/{productUuid}")
  public ProductDto updateProduct(
      @PathVariable UUID productUuid, @RequestBody @Valid ProductDto productDto) {
    return productService.updateProduct(productUuid, productDto);
  }

  @DeleteMapping("/{productUui}")
  public void deleteProduct(@PathVariable UUID uuid) {
    productService.deleteProduct(uuid);
  }
}
