package me.heyner.inventorypro.service;

import jakarta.validation.Valid;
import me.heyner.inventorypro.dto.LoginUserDto;
import me.heyner.inventorypro.dto.RegisterUserDto;
import me.heyner.inventorypro.model.User;
import me.heyner.inventorypro.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final AuthenticationManager authenticationManager;

  public AuthenticationService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
  }

  public User signUp(@Valid RegisterUserDto registerUserDto) {
    User user =
        new User()
            .setAuthority("USER")
            .setUsername(registerUserDto.getUsername())
            .setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

    return userRepository.save(user);
  }

  public User authenticate(@Valid LoginUserDto loginUserDto) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginUserDto.getEmail(), loginUserDto.getPassword()));

    return userRepository.findByEmail(loginUserDto.getEmail()).orElseThrow();
  }
}
