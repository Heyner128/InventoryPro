package me.heyner.inventorypro.repository;

import java.util.List;
import java.util.UUID;
import me.heyner.inventorypro.model.Option;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OptionRepository extends CrudRepository<Option, Long> {

  @Query("select o from Option o where o.product.id = ?1")
  List<Option> findByProduct_Id(UUID id);
}
