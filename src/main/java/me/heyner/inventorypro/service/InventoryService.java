package me.heyner.inventorypro.service;

import java.util.List;
import me.heyner.inventorypro.exception.InventoryNotFoundException;
import me.heyner.inventorypro.exception.UserNotFoundException;
import me.heyner.inventorypro.model.Inventory;
import me.heyner.inventorypro.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
  private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

  private final InventoryRepository inventoryRepository;

  private final ApplicationUserService applicationUserService;

  private final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

  public InventoryService(
      InventoryRepository inventoryRepository, ApplicationUserService applicationUserService) {
    this.inventoryRepository = inventoryRepository;
    this.applicationUserService = applicationUserService;
  }

  public Inventory addInventory(Inventory inventory) {
    inventoryRepository.save(inventory);
    logger.info("Inventory saved: {}", inventory);
    return inventory;
  }

  public Inventory updateInventory(Inventory inventory) {
    inventoryRepository.save(inventory);
    logger.info("Inventory updated: {}", inventory);
    return inventory;
  }

  public void deleteInventory(Long id) {
    inventoryRepository.deleteById(id);
    logger.info("Inventory deleted: {}", id);
  }

  public Inventory getInventory(Long id) throws InventoryNotFoundException {
    Inventory inventory =
        inventoryRepository.findById(id).orElseThrow(() -> new InventoryNotFoundException(id));
    logger.info("Inventory found: {}", inventory);
    return inventory;
  }

  public List<Inventory> getInventoriesByUsername(String username) throws UserNotFoundException {
    var user = applicationUserService.loadUserByUsername(username);
    List<Inventory> inventories = inventoryRepository.findAllByUsername(username);
    logger.info("Getting {} inventories for user {}", inventories.size(), user);
    return inventories;
  }
}
