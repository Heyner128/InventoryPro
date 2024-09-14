package me.heyner.inventorypro.service;

import java.util.List;
import java.util.UUID;
import me.heyner.inventorypro.dto.InventoryDto;
import me.heyner.inventorypro.exception.EntityNotFoundException;
import me.heyner.inventorypro.model.Inventory;
import me.heyner.inventorypro.model.User;
import me.heyner.inventorypro.repository.InventoryRepository;
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

  private final UserService userService;

  private final ModelMapper modelMapper = new ModelMapper();

  public InventoryService(InventoryRepository inventoryRepository, UserService userService) {
    this.inventoryRepository = inventoryRepository;
    this.userService = userService;
  }

  public InventoryDto addInventory(String username, InventoryDto inventoryDto)
      throws EntityNotFoundException {
    Inventory inventory = modelMapper.map(inventoryDto, Inventory.class);
    User user = userService.loadUserByUsername(username);
    inventory.setUser(user);
    Inventory savedInventory = inventoryRepository.save(inventory);
    logger.info("Inventory saved: {}", savedInventory);
    return modelMapper.map(savedInventory, InventoryDto.class);
  }

  public InventoryDto updateInventory(UUID uuid, InventoryDto inventoryDto)
      throws EntityNotFoundException {
    Inventory inventory =
        inventoryRepository
            .findById(uuid)
            .orElseThrow(() -> new EntityNotFoundException("not found"));
    inventory.setName(inventoryDto.getName());
    inventoryRepository.save(inventory);
    logger.info("Inventory updated: {}", inventory);
    return modelMapper.map(inventory, InventoryDto.class);
  }

  public void deleteInventory(UUID uuid) throws EntityNotFoundException {
    inventoryRepository.deleteById(uuid);
    logger.info("Inventory deleted: {}", uuid);
  }

  public InventoryDto getInventory(UUID uuid) throws EntityNotFoundException {

    Inventory inventory =
        inventoryRepository
            .findById(uuid)
            .orElseThrow(() -> new EntityNotFoundException("Inventory not found"));

    return modelMapper.map(inventory, InventoryDto.class);
  }

  public List<InventoryDto> getInventoriesByUsername(String username)
      throws UsernameNotFoundException {
    UserDetails user = userService.loadUserByUsername(username);
    List<Inventory> inventories = inventoryRepository.findByUser_username(username);
    logger.info("Getting {} inventories for user {}", inventories.size(), user);
    return inventories.stream().map(inv -> modelMapper.map(inv, InventoryDto.class)).toList();
  }
}
