package me.heyner.inventorypro.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.heyner.inventorypro.dto.InventoryDto;
import me.heyner.inventorypro.model.Authority;
import me.heyner.inventorypro.model.Inventory;
import me.heyner.inventorypro.model.User;
import me.heyner.inventorypro.repository.InventoryRepository;
import me.heyner.inventorypro.service.InventoryService;
import me.heyner.inventorypro.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class InventoryServiceTests {
  private final InventoryService inventoryService;
  private final ModelMapper modelMapper = new ModelMapper();
  @MockBean private InventoryRepository inventoryRepository;
  @MockBean private UserService userService;
  private User user;
  private Inventory inventory;

  @Autowired
  public InventoryServiceTests(
      InventoryService inventoryService,
      InventoryRepository inventoryRepository,
      UserService userService) {
    this.inventoryService = inventoryService;
    this.inventoryRepository = inventoryRepository;
    this.userService = userService;
  }

  public void setUpMockUser() {
    user = new User();
    user.setId(1L);
    user.setUsername("test");
    user.setPassword("teeeST1@");
    user.setEmail("test@test.com");
    user.setAuthorities(List.of(Authority.USER));
  }

  public void setupMockInventory() {
    inventory = new Inventory();
    inventory.setId(UUID.randomUUID());
    inventory.setName("My inventory");
    inventory.setUser(user);
  }

  public void setUpMockBeans() {
    when(userService.loadUserByUsername(user.getUsername())).thenReturn(user);
    when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);
    when(inventoryRepository.findById(inventory.getId())).thenReturn(Optional.of(inventory));
    when(inventoryRepository.findByUser_username(user.getUsername()))
        .thenReturn(List.of(inventory));
  }

  @BeforeEach
  void setUp() {
    setUpMockUser();
    setupMockInventory();
    setUpMockBeans();
  }

  @Test
  public void addInventory() {
    InventoryDto inventoryDto = modelMapper.map(inventory, InventoryDto.class);
    InventoryDto newInventoryDto = inventoryService.addInventory(user.getUsername(), inventoryDto);
    assertEquals(newInventoryDto, inventoryDto);
  }

  @Test
  public void updateInventory() {
    InventoryDto inventoryDto = modelMapper.map(inventory, InventoryDto.class);
    InventoryDto savedInventoryDto =
        inventoryService.updateInventory(inventory.getId(), inventoryDto);
    assertEquals(inventoryDto, savedInventoryDto);
  }

  @Test
  public void getInventory() {
    InventoryDto inventoryDto = modelMapper.map(inventory, InventoryDto.class);
    InventoryDto gottenInventory = inventoryService.getInventory(inventory.getId());
    assertEquals(inventoryDto, gottenInventory);
  }

  @Test
  public void getInventoriesByUsername() {
    InventoryDto inventoryDto = modelMapper.map(inventory, InventoryDto.class);
    List<InventoryDto> inventoriesDto =
        inventoryService.getInventoriesByUsername(user.getUsername());
    assertEquals(inventoryDto, inventoriesDto.getFirst());
  }
}
