package me.heyner.inventorypro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Inventory {
  @JsonIgnore @Id @GeneratedValue private Long id;

  @NotBlank(message = "The name of an inventory cannot be empty")
  private String name;

  @ElementCollection
  @CollectionTable(
      name = "inventory_quantity",
      joinColumns = @JoinColumn(name = "inventory_id", referencedColumnName = "id"))
  @Column(name = "quantity")
  @MapKeyJoinColumn(name = "sku_id", referencedColumnName = "id")
  private Map<SKU, Integer> items;

  @ManyToOne @NotNull private ApplicationUser user;

  @CreatedDate private LocalDate createdDate;

  @UpdateTimestamp private LocalDate updateDate;

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> objectEffectiveClass =
        o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != objectEffectiveClass) {
      return false;
    }
    Option option = (Option) o;
    return getId() != null && Objects.equals(getId(), option.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
