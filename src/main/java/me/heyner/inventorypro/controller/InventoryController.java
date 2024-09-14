package me.heyner.inventorypro.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import me.heyner.inventorypro.dto.InventoryDto;
import me.heyner.inventorypro.service.InventoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{username}/inventory")
public class InventoryController {

  private final InventoryService inventoryService;

  public InventoryController(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @PostMapping
  public InventoryDto createInventory(
      @PathVariable String username, @RequestBody @Valid InventoryDto inventoryDto) {
    return inventoryService.addInventory(username, inventoryDto);
  }

  @GetMapping("/{inventoryUuid}")
  public InventoryDto getInventory(@PathVariable UUID inventoryUuid) {
    return inventoryService.getInventory(inventoryUuid);
  }

  @PutMapping("/{inventoryUuid}")
  public InventoryDto updateInventory(
      @PathVariable UUID inventoryUuid, @RequestBody @Valid InventoryDto inventoryDto) {
    return inventoryService.updateInventory(inventoryUuid, inventoryDto);
  }

  @DeleteMapping("/{inventoryUuid}")
  public void deleteInventory(@PathVariable UUID inventoryUuid) {
    inventoryService.deleteInventory(inventoryUuid);
  }
}
