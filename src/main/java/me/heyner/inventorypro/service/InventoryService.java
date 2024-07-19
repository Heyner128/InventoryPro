package me.heyner.inventorypro.service;

import java.util.List;
import me.heyner.inventorypro.dto.InventoryDto;
import me.heyner.inventorypro.exception.InventoryNotFoundException;
import me.heyner.inventorypro.exception.UserNotFoundException;
import me.heyner.inventorypro.model.ApplicationUser;
import me.heyner.inventorypro.model.Inventory;
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

  private final ApplicationUserService applicationUserService;

  private final ModelMapper modelMapper = new ModelMapper();

  public InventoryService(
      InventoryRepository inventoryRepository, ApplicationUserService applicationUserService) {
    this.inventoryRepository = inventoryRepository;
    this.applicationUserService = applicationUserService;
  }

  public Inventory addInventory(String username, InventoryDto inventoryDto)
      throws UserNotFoundException {
    Inventory inventory = modelMapper.map(inventoryDto, Inventory.class);
    ApplicationUser user = applicationUserService.getApplicationUser(username);
    inventory.setUser(user);
    inventoryRepository.save(inventory);
    logger.info("Inventory saved: {}", inventory);
    return inventory;
  }

  public Inventory updateInventory(String username, int index, InventoryDto inventoryDto)
      throws InventoryNotFoundException, UserNotFoundException {
    Inventory inventory = getInventory(username, index);
    inventory.setName(inventoryDto.getName());
    inventoryRepository.save(inventory);
    logger.info("Inventory updated: {}", inventory);
    return inventory;
  }

  public void deleteInventory(String username, int index)
      throws InventoryNotFoundException, UserNotFoundException {
    List<Inventory> inventories = getInventoriesByUsername(username);
    try {
      Inventory inventory = inventories.get(index);
      inventoryRepository.deleteById(inventory.getId());
      logger.info("Inventory deleted: {}", inventory.getId());
    } catch (IndexOutOfBoundsException ex) {
      logger.error(ex.getMessage(), ex);
      throw new InventoryNotFoundException("Not found");
    }
  }

  public Inventory getInventory(String username, int index)
      throws InventoryNotFoundException, UserNotFoundException {
    List<Inventory> inventories = getInventoriesByUsername(username);
    try {
      Inventory inventory = inventories.get(index);
      logger.info("Inventory found: {}", inventory);
      return inventory;
    } catch (IndexOutOfBoundsException ex) {
      logger.error(ex.getMessage(), ex);
      throw new InventoryNotFoundException("Not found");
    }
  }

  public List<Inventory> getInventoriesByUsername(String username)
      throws UsernameNotFoundException {
    UserDetails user = applicationUserService.loadUserByUsername(username);
    List<Inventory> inventories = inventoryRepository.findByUser_username(username);
    logger.info("Getting {} inventories for user {}", inventories.size(), user);
    return inventories;
  }
}
