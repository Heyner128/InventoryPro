package me.heyner.inventorypro.repository;

import java.util.Optional;
import me.heyner.inventorypro.model.ApplicationUser;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Long> {
  Optional<ApplicationUser> findUserByUsername(String username);
}
