package me.heyner.inventorypro.repository;

import java.util.List;
import java.util.UUID;
import me.heyner.inventorypro.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, UUID> {

  @Query("select p from Product p where p.user.username = ?1")
  List<Product> findByUser_username(String username);
}
