package me.heyner.inventorypro.unit.service;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.heyner.inventorypro.model.*;
import me.heyner.inventorypro.repository.OptionRepository;
import me.heyner.inventorypro.repository.ProductRepository;
import me.heyner.inventorypro.service.OptionService;
import me.heyner.inventorypro.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class OptionServiceTests {

  private final OptionService optionService;

  private final ModelMapper modelMapper = new ModelMapper();

  private Option mockOption;

  private Product mockProduct;

  @MockBean private OptionRepository optionRepository;

  @MockBean private ProductService productService;

  @Autowired private ProductRepository productRepository;

  @Autowired
  public OptionServiceTests(
      OptionRepository optionRepository,
      ProductService productService,
      OptionService optionService) {
    this.optionRepository = optionRepository;
    this.productService = productService;
    this.optionService = optionService;
  }

  public void setUpMocks() {
    mockProduct =
        new Product()
            .setId(UUID.randomUUID())
            .setName("Shirt")
            .setDescription("A t-shirt")
            .setBrand("Adidas")
            .setUser(
                new User()
                    .setId(1L)
                    .setEmail("test@test.com")
                    .setPassword("TEst@1")
                    .setUsername("test")
                    .setAuthorities(List.of(Authority.USER))
                    .setProducts(List.of(mockProduct)));

    mockOption =
        new Option()
            .setId(1L)
            .setName("Color")
            .setProduct(mockProduct)
            .setValues(List.of(new OptionValue().setId(1L).setValue("Red").setOption(mockOption)));
  }

  @BeforeEach
  public void setUp() {
    setUpMocks();
    when(productRepository.findById(mockProduct.getId()))
        .thenReturn(Optional.ofNullable(mockProduct));

    when(productRepository.save(mockProduct)).thenReturn(mockProduct);

    when(optionRepository.save(mockOption)).thenReturn(mockOption);

    when(optionRepository.findByProduct_Id(mockProduct.getId())).thenReturn(List.of(mockOption));
  }
}
