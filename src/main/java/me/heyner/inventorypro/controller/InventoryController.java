package me.heyner.inventorypro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import me.heyner.inventorypro.dto.InventoryInputDto;
import me.heyner.inventorypro.dto.InventoryItemInputDto;
import me.heyner.inventorypro.dto.InventoryItemOutputDto;
import me.heyner.inventorypro.dto.InventoryOutputDto;
import me.heyner.inventorypro.model.Inventory;
import me.heyner.inventorypro.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@SecurityRequirement(name = "JWT token")
@Tag(name = "Inventories")
@RestController
@RequestMapping("/users/{username}/inventory")
public class InventoryController {

  private final InventoryService inventoryService;

  public InventoryController(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @GetMapping
  public List<InventoryOutputDto> getMethodName(@PathVariable String username) {
      return inventoryService.getInventoriesByUsername(username);
  }
  
  
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public InventoryOutputDto createInventory(
      @PathVariable String username, @RequestBody @Valid InventoryInputDto inventoryInputDto) {
    return inventoryService.addInventory(username, inventoryInputDto);
  }

  @GetMapping("/{inventoryUuid}")
  public InventoryOutputDto getInventory(@PathVariable UUID inventoryUuid) {
    return inventoryService.getInventory(inventoryUuid);
  }

  @PutMapping("/{inventoryUuid}")
  public InventoryOutputDto updateInventory(
      @PathVariable UUID inventoryUuid, @RequestBody @Valid InventoryInputDto inventoryInputDto) {
    return inventoryService.updateInventory(inventoryUuid, inventoryInputDto);
  }

  @DeleteMapping("/{inventoryUuid}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteInventory(@PathVariable UUID inventoryUuid) {
    inventoryService.deleteInventory(inventoryUuid);
  }

  @GetMapping("/{inventoryUuid}/item")
  public List<InventoryItemOutputDto> getInventoryItem(@PathVariable UUID inventoryUuid) {
    return inventoryService.getInventory(inventoryUuid).getItems();
  }

  @PostMapping("/{inventoryUuid}/item")
  @ResponseStatus(HttpStatus.CREATED)
  public InventoryItemOutputDto addInventoryItem(
      @RequestBody @Valid InventoryItemInputDto inventoryItemInputDto,
      @PathVariable UUID inventoryUuid) {
    return inventoryService.addOrUpdateInventoryItem(
        inventoryUuid, inventoryItemInputDto.getSkuId(), inventoryItemInputDto.getQuantity());
  }

  @DeleteMapping("/{inventoryUuid}/item/{skuUuid}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteInventoryItem(@PathVariable UUID inventoryUuid, @PathVariable UUID skuUuid) {
    inventoryService.deleteInventoryItem(inventoryUuid, skuUuid);
  }
}
