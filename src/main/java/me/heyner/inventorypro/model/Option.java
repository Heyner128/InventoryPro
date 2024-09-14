package me.heyner.inventorypro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@ToString
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "name"}))
@NoArgsConstructor
public class Option {

  @JsonIgnore
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Product product;

  @OneToMany(mappedBy = "option", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<OptionValue> values;

  @OneToMany(mappedBy = "option", fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<SKUValue> skuValues;

  @CreatedDate private Date createdDate;

  @UpdateTimestamp private Date updateDate;

  public Option setProduct(Product product) {
    product.getOptions().add(this);
    this.product = product;
    return this;
  }

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
