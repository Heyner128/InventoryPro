package me.heyner.inventorypro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Product {
  @JsonIgnore @Id @GeneratedValue private Long id;

  @NotBlank(message = "The name of a product can't be empty")
  @Column(unique = true, nullable = false)
  private String name;

  @NotBlank(message = "The description of a product can't be empty")
  private String description;

  @NotBlank(message = "The brand of a product can't be empty")
  private String brand;

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<Option> options;

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<SKU> skus;

  @ManyToOne(fetch = FetchType.EAGER)
  @NotNull
  private User user;

  @CreatedDate private LocalDate createdDate;

  @UpdateTimestamp private LocalDate updateDate;

  @JsonProperty("id")
  public final int getIndex() {
    return user.getProducts().indexOf(this);
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass =
        o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    Product product = (Product) o;
    return getId() != null && Objects.equals(getId(), product.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
