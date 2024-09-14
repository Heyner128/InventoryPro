package me.heyner.inventorypro.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import me.heyner.inventorypro.dto.ProductDto;
import me.heyner.inventorypro.exception.EntityNotFoundException;
import me.heyner.inventorypro.model.Product;
import me.heyner.inventorypro.model.User;
import me.heyner.inventorypro.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

  private final ProductRepository productRepository;

  private final UserService userService;

  @PersistenceContext private final EntityManager entityManager;

  private final ModelMapper modelMapper = new ModelMapper();

  public ProductService(
      ProductRepository productRepository, EntityManager entityManager, UserService userService) {
    this.productRepository = productRepository;
    this.entityManager = entityManager;
    this.userService = userService;
  }

  public List<ProductDto> getProducts(String username) throws EntityNotFoundException {
    try {
      List<Product> products = productRepository.findByUser_username(username);
      logger.info("{} products found for user {}", products.size(), username);
      return products.stream().map(pr -> modelMapper.map(pr, ProductDto.class)).toList();
    } catch (RuntimeException ex) {
      logger.error(ex.getMessage(), ex);
      throw new EntityNotFoundException("Not found");
    }
  }

  public ProductDto createProduct(String username, ProductDto productDto)
      throws EntityNotFoundException {
    Product product = modelMapper.map(productDto, Product.class);
    User user = userService.loadUserByUsername(username);
    product.setUser(user);
    Product savedProduct = productRepository.save(product);
    logger.info("{} Product successfully created", product.getName());
    return modelMapper.map(savedProduct, ProductDto.class);
  }

  public ProductDto getProduct(UUID uuid) throws EntityNotFoundException {
    Product product =
        productRepository
            .findById(uuid)
            .orElseThrow(() -> new EntityNotFoundException("Not found"));
      logger.info("Product {} found", product.getName());
    return modelMapper.map(product, ProductDto.class);
  }

  public ProductDto updateProduct(UUID uuid, ProductDto productDto) throws EntityNotFoundException {
    Product productToUpdate =
        productRepository
            .findById(uuid)
            .orElseThrow(() -> new EntityNotFoundException("Not found"));
    productToUpdate.setName(productDto.getName());
    productToUpdate.setDescription(productDto.getDescription());
    productToUpdate.setBrand(productDto.getBrand());
    Product savedProduct = productRepository.save(productToUpdate);
    logger.info("{} Product successfully updated", productToUpdate.getName());
    return modelMapper.map(savedProduct, ProductDto.class);
  }

  public void deleteProduct(UUID uuid) throws EntityNotFoundException {
    productRepository.deleteById(uuid);
    logger.info("{} Product successfully deleted", uuid);
  }
}
