package me.heyner.inventorypro.controller;

import jakarta.validation.Valid;
import me.heyner.inventorypro.dto.InventoryDto;
import me.heyner.inventorypro.model.Inventory;
import me.heyner.inventorypro.service.InventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{username}/inventory")
public class InventoryController {

  private final InventoryService inventoryService;

  private final ModelMapper modelMapper = new ModelMapper();

  public InventoryController(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @PostMapping
  public Inventory createInventory(
      @PathVariable String username, @RequestBody @Valid InventoryDto inventoryDto) {
    return inventoryService.addInventory(username, inventoryDto);
  }

  @GetMapping("/{inventoryIndex}")
  public Inventory getInventory(@PathVariable String username, @PathVariable int inventoryIndex) {
    return inventoryService.getInventory(username, inventoryIndex);
  }

  @PutMapping("/{inventoryIndex}")
  public Inventory updateInventory(
      @PathVariable String username,
      @PathVariable int inventoryIndex,
      @RequestBody @Valid InventoryDto inventoryDto) {
    Inventory inventory = modelMapper.map(inventoryDto, Inventory.class);
    return inventoryService.updateInventory(username, inventoryIndex, inventoryDto);
  }

  @DeleteMapping("/{inventoryIndex}")
  public void deleteInventory(@PathVariable String username, @PathVariable int inventoryIndex) {
    inventoryService.deleteInventory(username, inventoryIndex);
  }
}
