package me.heyner.inventorypro.controller;

import jakarta.validation.Valid;
import me.heyner.inventorypro.exception.ConflictingIndexesException;
import me.heyner.inventorypro.model.Product;
import me.heyner.inventorypro.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{productId}")
    public Product getProduct(@PathVariable Long productId) {
        return productService.findById(productId);
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody @Valid Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/products/{productId}")
    public Product updateProduct(@PathVariable Long productId, @RequestBody @Valid Product product) {
        if(product.getId() != null && !product.getId().equals(productId)) throw new ConflictingIndexesException();
        product.setId(productId);
        return productService.updateProduct(product);
    }

    @DeleteMapping("/products/{productId}")
    public void deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
    }
}
