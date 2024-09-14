package me.heyner.inventorypro.controller;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import me.heyner.inventorypro.dto.LoginUserDto;
import me.heyner.inventorypro.dto.RegisterUserDto;
import me.heyner.inventorypro.dto.UserDto;
import me.heyner.inventorypro.service.AuthenticationService;
import me.heyner.inventorypro.service.JwtService;
import me.heyner.inventorypro.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  private final AuthenticationService authenticationService;

  private final JwtService jwtService;

  private final ModelMapper modelMapper = new ModelMapper();

  public UserController(
      UserService userService, JwtService jwtService, AuthenticationService authenticationService) {
    this.userService = userService;
    this.jwtService = jwtService;
    this.authenticationService = authenticationService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserDto register(@RequestBody @Valid RegisterUserDto registerUserDto) {
    return authenticationService.signUp(registerUserDto);
  }

  @PostMapping("/login")
  public Map<String, String> login(@RequestBody @Valid LoginUserDto loginUserDto) {
    UserDto authenticatedUser = authenticationService.authenticate(loginUserDto);

    String jwtToken =
        jwtService.generateToken(userService.loadUserByUsername(authenticatedUser.getUsername()));

    Map<String, String> response = new HashMap<>();

    response.put("token", jwtToken);
    response.put("username", authenticatedUser.getUsername());
    return response;
  }

  @GetMapping("/{username}")
  public UserDto getUser(@PathVariable String username) {
    return modelMapper.map(userService.loadUserByUsername(username), UserDto.class);
  }

  @PutMapping("/{username}")
  public UserDto updateUser(@PathVariable String username, @RequestBody @Valid UserDto userDto) {
    return userService.updateUser(username, userDto);
  }
}
