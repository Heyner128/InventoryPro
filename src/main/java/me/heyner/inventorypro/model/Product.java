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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "name"})})
@NoArgsConstructor
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String brand;

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<Option> options;

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<SKU> skus;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = false)
  private User user;

  @CreatedDate private Date createdDate;

  @UpdateTimestamp private Date updateDate;

  public Product addOption(Option option) {
    this.getOptions().add(option);
    option.setProduct(this);
    return this;
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
