package me.heyner.inventorypro.repository;

import me.heyner.inventorypro.model.ApplicationUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Long> {
  Optional<ApplicationUser> findUserByUsername(String username);
}
