package me.heyner.inventorypro.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.heyner.inventorypro.model.Authority;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode
public class UserDto {
  @Setter @NotNull private String username;

  @NotNull @Email @Setter private String email;

  @Size(min = 1)
  private List<Authority> authorities;

  // Spring Security shit don't touch
  public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
    this.authorities =
        authorities.stream().map(authority -> Authority.valueOf(authority.getAuthority())).toList();
  }

  @JsonSetter
  public UserDto setAuthorities(List<Authority> authorities) {
    this.authorities = authorities;
    return this;
  }
}