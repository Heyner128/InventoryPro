package me.heyner.inventorypro.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import me.heyner.inventorypro.model.Option;
import me.heyner.inventorypro.model.Product;
import me.heyner.inventorypro.model.SKU;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SKURepository extends CrudRepository<SKU, Long> {
    Optional<SKU> findBySkuIgnoreCaseAndProduct(@NotBlank(message = "The name of a sku can't be empty") String sku, @NotNull(message = "A SKU should have an associated product") Product product);
}
