package me.heyner.inventorypro.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@ToString
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"product_id", "sku"})})
@NoArgsConstructor
public class SKU {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String sku;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Product product;

  @OneToMany(mappedBy = "sku", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<SKUValue> values;

  @CreatedDate private Date createdDate;

  @UpdateTimestamp private Date updateDate;

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
