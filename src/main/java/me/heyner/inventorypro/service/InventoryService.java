package me.heyner.inventorypro.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.heyner.inventorypro.dto.InventoryInputDto;
import me.heyner.inventorypro.dto.InventoryItemOutputDto;
import me.heyner.inventorypro.exception.EntityNotFoundException;
import me.heyner.inventorypro.model.Inventory;
import me.heyner.inventorypro.model.SKU;
import me.heyner.inventorypro.model.User;
import me.heyner.inventorypro.repository.InventoryRepository;
import me.heyner.inventorypro.repository.SKURepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

  private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

  private final InventoryRepository inventoryRepository;

  private final SKURepository skuRepository;

  private final UserService userService;

  private final ModelMapper modelMapper = new ModelMapper();

  public InventoryService(
      InventoryRepository inventoryRepository,
      SKURepository skuRepository,
      UserService userService) {
    this.inventoryRepository = inventoryRepository;
    this.skuRepository = skuRepository;
    this.userService = userService;
  }

  public List<InventoryItemOutputDto> mapInventoryItemsOutputDto(Inventory inventory) {
    Map<SKU, Integer> items = inventory.getItems();

    return items.entrySet().stream()
        .map(
            skuIntegerEntry ->
                new InventoryItemOutputDto()
                    .setSkuId(skuIntegerEntry.getKey().getId())
                    .setSkuName(skuIntegerEntry.getKey().getSku())
                    .setBrand(skuIntegerEntry.getKey().getProduct().getBrand())
                    .setCostPrice(skuIntegerEntry.getKey().getCostPrice())
                    .setAmountAvailable(skuIntegerEntry.getKey().getAmountAvailable())
                    .setMarginPercentage(skuIntegerEntry.getKey().getMarginPercentage())
                    .setOption(skuIntegerEntry.getKey().getOption())
                    .setQuantity(skuIntegerEntry.getValue()))
        .toList();
  }

  public Inventory addInventory(String username, InventoryInputDto inventoryInputDto)
      throws EntityNotFoundException {
    Inventory inventory = modelMapper.map(inventoryInputDto, Inventory.class);
    User user = userService.loadUserByUsername(username);
    inventory.setUser(user);
    Inventory savedInventory = inventoryRepository.save(inventory);
    logger.info("Inventory saved: {}", savedInventory);
    return savedInventory;
  }

  public Inventory updateInventory(UUID uuid, InventoryInputDto inventoryInputDto)
      throws EntityNotFoundException {
    Inventory inventory =
        inventoryRepository
            .findById(uuid)
            .orElseThrow(() -> new EntityNotFoundException("not found"));
    inventory.setName(inventoryInputDto.getName());
    Inventory savedInventory = inventoryRepository.save(inventory);
    logger.info("Inventory updated: {}", inventory);
    return savedInventory;
  }

  public InventoryItemOutputDto addOrUpdateInventoryItem(UUID uuid, UUID skuUuid, Integer quantity)
      throws EntityNotFoundException {
    Inventory inventory =
        inventoryRepository
            .findById(uuid)
            .orElseThrow(() -> new EntityNotFoundException("not found"));

    SKU sku =
        skuRepository.findById(skuUuid).orElseThrow(() -> new EntityNotFoundException("not found"));

    inventory.getItems().put(sku, quantity);

    Inventory savedInventory = inventoryRepository.save(inventory);

    logger.info("Item {} added to inventory {}", sku.getSku(), inventory.getId());

    return mapInventoryItemsOutputDto(savedInventory).getLast();
  }

  public void deleteInventoryItem(UUID uuid, UUID skuUuid) throws EntityNotFoundException {
    Inventory inventory =
        inventoryRepository
            .findById(uuid)
            .orElseThrow(() -> new EntityNotFoundException("not found"));

    SKU sku =
        skuRepository.findById(skuUuid).orElseThrow(() -> new EntityNotFoundException("not found"));

    var removedItem = inventory.getItems().remove(sku);

    if (removedItem == null) {
      throw new EntityNotFoundException("not found");
    }

    logger.info("Item {} removed from inventory {}", sku.getSku(), inventory.getId());
  }

  public void deleteInventory(UUID uuid) throws EntityNotFoundException {
    inventoryRepository.deleteById(uuid);
    logger.info("Inventory deleted: {}", uuid);
  }

  public Inventory getInventory(UUID uuid) throws EntityNotFoundException {

    return inventoryRepository
        .findById(uuid)
        .orElseThrow(() -> new EntityNotFoundException("Inventory not found"));
  }

  public List<Inventory> getInventoriesByUsername(String username)
      throws UsernameNotFoundException {
    UserDetails user = userService.loadUserByUsername(username);
    List<Inventory> inventories = inventoryRepository.findByUser_username(username);
    logger.info("Getting {} inventories for user {}", inventories.size(), user);
    return inventories.stream().toList();
  }
}
