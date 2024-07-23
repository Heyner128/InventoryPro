package me.heyner.inventorypro.controller;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import me.heyner.inventorypro.dto.LoginUserDto;
import me.heyner.inventorypro.dto.RegisterUserDto;
import me.heyner.inventorypro.exception.ConflictingIndexesException;
import me.heyner.inventorypro.model.User;
import me.heyner.inventorypro.service.AuthenticationService;
import me.heyner.inventorypro.service.JwtService;
import me.heyner.inventorypro.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  private final AuthenticationService authenticationService;

  private final JwtService jwtService;

  public UserController(
      UserService userService, AuthenticationService authenticationService, JwtService jwtService) {
    this.userService = userService;
    this.authenticationService = authenticationService;
    this.jwtService = jwtService;
  }

  @PostMapping
  public User register(@RequestBody @Valid RegisterUserDto registerUserDto) {
    return authenticationService.signUp(registerUserDto);
  }

  @PostMapping("/login")
  public Map<String, String> login(@RequestBody @Valid LoginUserDto loginUserDto) {
    User authenticatedUSer = authenticationService.authenticate(loginUserDto);

    String jwtToken = jwtService.generateToken(authenticatedUSer);

    Map<String, String> response = new HashMap<>();

    response.put("token", jwtToken);
    response.put("username", authenticatedUSer.getUsername());
    return response;
  }

  @GetMapping("/{username}")
  public User getUser(@PathVariable String username) {
    return userService.loadUserByUsername(username);
  }

  @PutMapping("/{username}")
  public User updateUser(@PathVariable String username, @RequestBody @Valid User user)
      throws ConflictingIndexesException {
    if (!username.equals(user.getUsername())) {
      throw new ConflictingIndexesException();
    }
    return userService.saveApplicationUser(user);
  }
}
