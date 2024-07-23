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
public class SKU {
  @Id @GeneratedValue @JsonIgnore private Long id;

  @NotBlank(message = "SKU name can't be blank")
  @Column(unique = true, nullable = false)
  private String sku;

  @ManyToOne @NotNull private Product product;

  @OneToMany(mappedBy = "sku", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<SKUValue> values;

  @CreatedDate private LocalDate createdDate;

  @UpdateTimestamp private LocalDate updateDate;

  @JsonProperty("id")
  public final int getIndex() {
    return product.getSkus().indexOf(this);
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
    SKU sku = (SKU) o;
    return getId() != null && Objects.equals(getId(), sku.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
