package me.heyner.inventorypro.repository;

import java.util.Optional;
import me.heyner.inventorypro.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);
}
