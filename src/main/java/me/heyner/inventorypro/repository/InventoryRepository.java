package me.heyner.inventorypro.repository;

import me.heyner.inventorypro.model.Inventory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {

  @Query("select i from Inventory i where i.user.username = ?1")
  List<Inventory> findAllByUsername(String username);
}
