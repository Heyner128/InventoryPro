package me.heyner.inventorypro.unit.service;

import me.heyner.inventorypro.repository.ProductRepository;
import me.heyner.inventorypro.service.UserService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceTests {
  // TODO - write me pls

  @MockBean private ProductRepository productRepository;

  @MockBean private UserService userService;

  public ProductServiceTests(ProductRepository productRepository, UserService userService) {
    this.productRepository = productRepository;
    this.userService = userService;
  }
}
