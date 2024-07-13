package me.heyner.inventorypro.exception;

import me.heyner.inventorypro.model.Inventory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class InventoryNotFoundException extends RuntimeException {
  public InventoryNotFoundException() {
    super("Inventory not found");
  }

  public InventoryNotFoundException(String message) {
    super(message);
  }

  public InventoryNotFoundException(Long inventoryId) {
    super("Inventory with the id " + inventoryId + "not found");
  }

  public InventoryNotFoundException(Inventory inventory) {
    super("Inventory " + inventory + " not found");
  }
}
