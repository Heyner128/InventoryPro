package me.heyner.inventorypro.controller;

import jakarta.validation.Valid;
import java.util.List;
import me.heyner.inventorypro.dto.ProductDto;
import me.heyner.inventorypro.model.Product;
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
  public List<Product> getAllProducts(@PathVariable String username) {
    return productService.getProducts(username);
  }

  @GetMapping("/{productIndex}")
  public Product getProduct(@PathVariable String username, @PathVariable int productIndex) {
    return productService.getProduct(username, productIndex);
  }

  @PostMapping
  public Product createProduct(
      @RequestBody @Valid ProductDto productDto, @PathVariable String username) {
    return productService.createProduct(username, productDto);
  }

  @PutMapping("/{productIndex}")
  public Product updateProduct(
      @PathVariable String username,
      @PathVariable int productIndex,
      @RequestBody @Valid ProductDto productDto) {
    return productService.updateProduct(username, productIndex, productDto);
  }

  @DeleteMapping("/{productIndex}")
  public void deleteProduct(@PathVariable String username, @PathVariable int productIndex) {
    productService.deleteProduct(username, productIndex);
  }
}
