package me.heyner.inventorypro.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.heyner.inventorypro.dto.*;
import me.heyner.inventorypro.model.Inventory;
import me.heyner.inventorypro.model.Option;
import me.heyner.inventorypro.model.Product;
import me.heyner.inventorypro.model.SKU;
import me.heyner.inventorypro.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryIntegrationTests {

  @Autowired TestRestTemplate restTemplate;

  @Autowired InventoryRepository inventoryRepository;

  @Autowired ProductRepository productRepository;

  @Autowired SKURepository skuRepository;

  @Autowired OptionRepository optionRepository;

  @Autowired UserRepository userRepository;

  private String loginToken;

  @BeforeEach
  public void setup() {
    String username = "test";
    String password = "teeeST@1";
    String email = "test@test.com";

    inventoryRepository.deleteAll();

    skuRepository.deleteAll();

    optionRepository.deleteAll();

    productRepository.deleteAll();

    userRepository.deleteAll();

    RegisterUserDto registerUserDto =
        new RegisterUserDto().setEmail(email).setUsername(username).setPassword(password);

    restTemplate.postForLocation("/users", registerUserDto);

    LoginUserDto loginUserDto = new LoginUserDto().setUsername(username).setPassword(password);

    this.loginToken =
        (String) restTemplate.postForObject("/users/login", loginUserDto, Map.class).get("token");
  }

  public ResponseEntity<Inventory> createInventory() {
    InventoryInputDto inventoryInputDto = new InventoryInputDto().setName("MyTestInventory");

    RequestEntity<InventoryInputDto> request =
        RequestEntity.post("/users/test/inventory")
            .header("Authorization", "Bearer " + loginToken)
            .body(inventoryInputDto, InventoryInputDto.class);
    return restTemplate.exchange(request, Inventory.class);
  }

  public ResponseEntity<Product> createProduct() {
    ProductInputDto productInputDto =
        new ProductInputDto()
            .setName("T-Shirt")
            .setBrand("Adidas")
            .setDescription("An adidas t-shirt");

    RequestEntity<ProductInputDto> requestProductCreation =
        RequestEntity.post("/users/test/products")
            .header("Authorization", "Bearer " + loginToken)
            .body(productInputDto, ProductInputDto.class);

    return restTemplate.exchange(requestProductCreation, Product.class);
  }

  public ResponseEntity<Option> createOption(UUID productUuid) {
    OptionInputDto optionInputDto =
        new OptionInputDto().setName("Color").setValues(List.of("Red", "Blue", "Orange"));

    RequestEntity<OptionInputDto> requestOptionCreation =
        RequestEntity.post("/users/test/products/" + productUuid + "/options")
            .header("Authorization", "Bearer " + loginToken)
            .body(optionInputDto, OptionInputDto.class);

    return restTemplate.exchange(requestOptionCreation, Option.class);
  }

  public ResponseEntity<SKU> createSKU(UUID productUuid, UUID optionUuid) {
    SKUInputDto skuInputDto =
        new SKUInputDto()
            .setSku("ADITSHIRT")
            .setCostPrice(new BigDecimal("9.99"))
            .setAmountAvailable(99L)
            .setMarginPercentage(10)
            .setOptionUUID(optionUuid)
            .setOptionValue("Red");

    RequestEntity<SKUInputDto> requestSKUCreation =
        RequestEntity.post("/users/test/products/" + productUuid + "/skus")
            .header("Authorization", "Bearer " + loginToken)
            .body(skuInputDto, SKUInputDto.class);

    return restTemplate.exchange(requestSKUCreation, SKU.class);
  }

  @Test
  void createInventoryTest() {
    ResponseEntity<Inventory> response = createInventory();
    assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
    assertThat(response.getBody().getName(), equalTo("MyTestInventory"));
  }

  @Test
  void getInventory() {
    Inventory createdInventory = createInventory().getBody();
    RequestEntity<Void> request =
        RequestEntity.get("/users/test/inventory/" + createdInventory.getId())
            .header("Authorization", "Bearer " + loginToken)
            .build();

    ResponseEntity<Inventory> response = restTemplate.exchange(request, Inventory.class);

    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    assertThat(response.getBody(), equalTo(createdInventory));
  }

  @Test
  void updateInventory() {
    Inventory createdInventory = createInventory().getBody();

    InventoryInputDto inventoryInputDto = new InventoryInputDto().setName("UpdatedTestInventory");

    RequestEntity<InventoryInputDto> request =
        RequestEntity.put("/users/test/inventory/" + createdInventory.getId())
            .header("Authorization", "Bearer " + loginToken)
            .body(inventoryInputDto, InventoryInputDto.class);

    ResponseEntity<Inventory> response = restTemplate.exchange(request, Inventory.class);

    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    assertThat(response.getBody().getName(), equalTo(inventoryInputDto.getName()));
  }

  @Test
  void deleteInventory() {
    Inventory createdInventory = createInventory().getBody();
    RequestEntity<Void> request =
        RequestEntity.delete("/users/test/inventory/" + createdInventory.getId())
            .header("Authorization", "Bearer " + loginToken)
            .build();

    ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

    assertThat(response.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
  }

  @Test
  void addInventoryItem() {
    Inventory createdInventory = createInventory().getBody();

    Product createdProduct = createProduct().getBody();

    Option createdOption = createOption(createdProduct.getId()).getBody();

    SKU createdSKU = createSKU(createdProduct.getId(), createdOption.getId()).getBody();

    InventoryItemInputDto inventoryItemInputDto =
        new InventoryItemInputDto().setQuantity(99).setSkuId(createdSKU.getId());

    RequestEntity<InventoryItemInputDto> requestInventoryCreation =
        RequestEntity.post("/users/test/inventory/" + createdInventory.getId() + "/item")
            .header("Authorization", "Bearer " + loginToken)
            .body(inventoryItemInputDto, InventoryItemInputDto.class);

    ResponseEntity<InventoryItemOutputDto> responseInventoryCreation =
        restTemplate.exchange(requestInventoryCreation, InventoryItemOutputDto.class);

    assertThat(responseInventoryCreation.getStatusCode(), equalTo(HttpStatus.CREATED));
  }

  @Test
  void deleteInventoryItem() {
    Inventory createdInventory = createInventory().getBody();

    Product createdProduct = createProduct().getBody();

    Option createdOption = createOption(createdProduct.getId()).getBody();

    SKU createdSKU = createSKU(createdProduct.getId(), createdOption.getId()).getBody();

    InventoryItemInputDto inventoryItemInputDto =
        new InventoryItemInputDto().setQuantity(99).setSkuId(createdSKU.getId());

    RequestEntity<InventoryItemInputDto> requestInventoryCreation =
        RequestEntity.post("/users/test/inventory/" + createdInventory.getId() + "/item")
            .header("Authorization", "Bearer " + loginToken)
            .body(inventoryItemInputDto, InventoryItemInputDto.class);

    restTemplate.exchange(requestInventoryCreation, InventoryItemOutputDto.class);

    RequestEntity<Void> requestInventoryItemDeletion =
        RequestEntity.delete("/users/test/inventory/" + createdInventory.getId())
            .header("Authorization", "Bearer " + loginToken)
            .build();

    ResponseEntity<Void> responseInventoryItemDeletion =
        restTemplate.exchange(requestInventoryItemDeletion, Void.class);

    assertThat(responseInventoryItemDeletion.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
  }
}
