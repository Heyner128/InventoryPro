package me.heyner.inventorypro.repository;

import java.util.List;
import me.heyner.inventorypro.model.Inventory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {

  @Query("select i from Inventory i where i.user.username = ?1")
  List<Inventory> findAllByUsername(String username);
}
