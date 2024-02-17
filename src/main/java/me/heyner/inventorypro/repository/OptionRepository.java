package me.heyner.inventorypro.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import me.heyner.inventorypro.model.Option;
import me.heyner.inventorypro.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OptionRepository extends CrudRepository<Option, Long> {

    Optional<Option> findByNameIgnoreCaseAndProduct(@NotBlank(message = "The name of an option can't be empty") String name, @NotNull(message = "An option should have an associated product") Product product);
}
