package me.heyner.inventorypro.controller;

import jakarta.validation.Valid;
import me.heyner.inventorypro.dto.InventoryDto;
import me.heyner.inventorypro.exception.ConflictingIndexesException;
import me.heyner.inventorypro.model.Inventory;
import me.heyner.inventorypro.service.InventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

@RestController
public class InventoryController {

    private final InventoryService inventoryService;

    private final ModelMapper modelMapper = new ModelMapper();

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

  @PostMapping("/inventory")
  public Inventory createInventory(@RequestBody @Valid InventoryDto inventoryDto) {
    return inventoryService.addInventory(modelMapper.map(inventoryDto, Inventory.class));
  }

  @GetMapping("/inventory/{id}")
  public Inventory getInventory(@PathVariable int id) {
    return inventoryService.getInventory((long) id);
  }

  @PutMapping("/inventory/{id}")
  public Inventory updateInventory(
      @PathVariable int id, @RequestBody @Valid InventoryDto inventoryDto)
      throws ConflictingIndexesException {
    Inventory inventory = modelMapper.map(inventoryDto, Inventory.class);
    if (inventory.getId() != id) throw new ConflictingIndexesException();
    return inventoryService.updateInventory(inventory);
  }

  @DeleteMapping("/inventory/{id}")
  public void deleteInventory(@PathVariable int id) {
    inventoryService.deleteInventory((long) id);
    }
}
