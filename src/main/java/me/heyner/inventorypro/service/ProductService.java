package me.heyner.inventorypro.service;

import me.heyner.inventorypro.exception.ProductNotFoundException;
import me.heyner.inventorypro.model.Product;
import me.heyner.inventorypro.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

  private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<Product> getAllProducts() {
    Iterator<Product> productIterator = productRepository.findAll().iterator();
    List<Product> products = new ArrayList<>();
    while (productIterator.hasNext()) {
      products.add(productIterator.next());
    }
    return products;
  }

  public Product createProduct(Product product) {
    productRepository.save(product);
    logger.info("{} Product successfully created", product.getName());
    return product;
  }

  public Product findById(Long id) throws ProductNotFoundException {
    Optional<Product> product = productRepository.findById(id);
    logger.info("Searching for product with the id {}", id);
    logger.info("Product with the id {} {} found", id, (product.isEmpty() ? "Not" : ""));
    return product.orElseThrow(() -> new ProductNotFoundException(id));
  }

  public List<Product> findByName(String name) {
    List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
    logger.info("Searching for products containing the name {}", name);
    logger.info("{} product{} found", products.size(), (products.size() > 1 ? "s" : ""));
    return products;
  }

  public List<Product> findByBrand(String name) {
    List<Product> products = productRepository.findByBrandContainingIgnoreCase(name);
    logger.info("Searching for products containing the brand {}", name);
    logger.info("{} product{} found", products.size(), (products.size() > 1 ? "s" : ""));
    return products;
  }

  public Product updateProduct(Product product) throws ProductNotFoundException {
    Product productToUpdate = findById(product.getId());
    productRepository.save(product);
    logger.info("Product {} successfully updated ", product.getName());
    return product;
  }

  public void deleteProduct(Long id) throws ProductNotFoundException {
    Product productToDelete = findById(id);

    productRepository.delete(productToDelete);

    logger.info("Product {} successfully deleted", productToDelete.getName());
  }
}
