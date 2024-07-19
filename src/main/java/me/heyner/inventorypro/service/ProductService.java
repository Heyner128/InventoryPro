package me.heyner.inventorypro.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import me.heyner.inventorypro.dto.ProductDto;
import me.heyner.inventorypro.exception.ProductNotFoundException;
import me.heyner.inventorypro.exception.UserNotFoundException;
import me.heyner.inventorypro.model.Product;
import me.heyner.inventorypro.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

  private final ProductRepository productRepository;

  @PersistenceContext private final EntityManager entityManager;

  public ProductService(ProductRepository productRepository, EntityManager entityManager) {
    this.productRepository = productRepository;
    this.entityManager = entityManager;
  }

  public List<Product> getProducts(String username) throws UserNotFoundException {
    try {
      List<Product> products = productRepository.findByUser_username(username);
      logger.info("{} products found for user {}", products.size(), username);
      return products;
    } catch (RuntimeException ex) {
      logger.error(ex.getMessage(), ex);
      throw new UserNotFoundException("Not found");
    }
  }

  public Product createProduct(Product product) {
    productRepository.save(product);
    logger.info("{} Product successfully created", product.getName());
    return product;
  }

  public Product getProduct(String username, int productIndex)
      throws ProductNotFoundException, UserNotFoundException {
    try {
      List<Product> products = productRepository.findByUser_username(username);
      Product product = products.get(productIndex);
      logger.info("Product {} found", product.getName());
      return product;
    } catch (IndexOutOfBoundsException ex) {
      logger.error(ex.getMessage(), ex);
      throw new ProductNotFoundException("Not found");
    } catch (RuntimeException ex) {
      logger.error(ex.getMessage(), ex);
      throw new UserNotFoundException("Not found");
    }
  }

  public Product getProductWithOptions(String username, int productIndex)
      throws UserNotFoundException, ProductNotFoundException {
    Product product = getProduct(username, productIndex);
    return entityManager
        .createQuery(
            """
           SELECT
            p
           FROM
            Product p
           JOIN FETCH
            p.options
           WHERE
            p.id = :id
        """,
            Product.class)
        .setParameter("id", product.getId())
        .getSingleResult();
  }

  public Product getProductWithSKU(String username, int productIndex)
      throws UserNotFoundException, ProductNotFoundException {
    Product product = getProduct(username, productIndex);
    return entityManager
        .createQuery(
            """
           SELECT
            p
           FROM
            Product p
           JOIN FETCH
            p.skus
           WHERE
            p.id = :id
        """,
            Product.class)
        .setParameter("id", product.getId())
        .getSingleResult();
  }

  public Product updateProduct(String username, int productIndex, ProductDto productDto)
      throws ProductNotFoundException {
    Product productToUpdate = getProduct(username, productIndex);
    productToUpdate.setName(productDto.getName());
    productToUpdate.setDescription(productDto.getDescription());
    productToUpdate.setBrand(productDto.getBrand());
    productRepository.save(productToUpdate);
    logger.info("{} Product successfully updated", productToUpdate.getName());
    return productToUpdate;
  }

  public void deleteProduct(String username, int productIndex) throws ProductNotFoundException {
    Product productToDelete = getProduct(username, productIndex);
    productRepository.delete(productToDelete);
    logger.info("{} Product successfully deleted", productToDelete.getName());
  }
}
