package me.heyner.inventorypro.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import me.heyner.inventorypro.controller.UserController;
import me.heyner.inventorypro.dto.LoginUserDto;
import me.heyner.inventorypro.dto.RegisterUserDto;
import me.heyner.inventorypro.dto.UserDto;
import me.heyner.inventorypro.model.Authority;
import me.heyner.inventorypro.model.User;
import me.heyner.inventorypro.service.AuthenticationService;
import me.heyner.inventorypro.service.JwtService;
import me.heyner.inventorypro.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(UserController.class)
public class UserControllerTests {

  private final MockMvc mockMvc;
  private final ObjectMapper jacksonObjectMapper;
  private final ModelMapper modelMapper = new ModelMapper();
  @MockBean private final JwtService jwtService;
  @MockBean private final UserService userService;
  @MockBean private final AuthenticationService authenticationService;
  private User user;

  @Autowired
  public UserControllerTests(
      MockMvc mockMvc,
      UserService userService,
      AuthenticationService authenticationService,
      JwtService jwtService,
      ObjectMapper jacksonObjectMapper) {
    this.mockMvc = mockMvc;
    this.userService = userService;
    this.jwtService = jwtService;
    this.authenticationService = authenticationService;
    this.jacksonObjectMapper = jacksonObjectMapper;
  }

  @BeforeEach
  void createMockUser() throws Exception {
    user = new User();
    user.setId(1L);
    user.setUsername("test");
    user.setPassword("teeeST1@");
    user.setEmail("test@test.com");
    user.setAuthorities(List.of(Authority.USER));
  }

  @Test
  @WithMockUser(
      username = "test",
      password = "teeeST1@",
      authorities = {"USER"})
  @DisplayName("POST on /users returns OK if the body is valid")
  void register() throws Exception {

    var mockRegisterUserDto = new RegisterUserDto();
    mockRegisterUserDto.setUsername("test");
    mockRegisterUserDto.setPassword("teeeST1@");
    mockRegisterUserDto.setEmail("test@test.com");

    when(authenticationService.signUp(mockRegisterUserDto))
        .thenReturn(modelMapper.map(user, UserDto.class));

    var requestBuilder =
        MockMvcRequestBuilders.post("/users")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(jacksonObjectMapper.writeValueAsString(mockRegisterUserDto));

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isCreated())
        .andExpect(
            content()
                .json(
                    jacksonObjectMapper.writeValueAsString(modelMapper.map(user, UserDto.class))));
  }

  @Test
  @WithMockUser(
      username = "test",
      password = "teeeST1@",
      authorities = {"USER"})
  @DisplayName("POST on /users/login returns OK if the body is valid")
  void login() throws Exception {
    var mockLogin = new LoginUserDto();

    mockLogin.setUsername("test");
    mockLogin.setPassword("teeeST1@");

    when(authenticationService.authenticate(mockLogin))
        .thenReturn(modelMapper.map(user, UserDto.class));
    when(userService.loadUserByUsername(user.getUsername())).thenReturn(user);
    when(jwtService.generateToken(any(User.class))).thenReturn("token");

    var requestBuilder =
        MockMvcRequestBuilders.post("/users/login")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(jacksonObjectMapper.writeValueAsString(mockLogin));

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json("{\"token\":  \"token\", \"username\":  \"" + user.getUsername() + "\"}"));
  }

  @Test
  @WithMockUser(
      username = "test",
      password = "teeeST1@",
      authorities = {"USER"})
  @DisplayName("GET on /users/test returns OK and the user information")
  void getInfo() throws Exception {
    when(userService.loadUserByUsername(user.getUsername())).thenReturn(user);

    var requestBuilder =
        MockMvcRequestBuilders.get("/users/test")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json(
                    jacksonObjectMapper.writeValueAsString(modelMapper.map(user, UserDto.class))));
  }

  @Test
  @WithMockUser(
      username = "test",
      password = "teeeST1@",
      authorities = {"USER"})
  @DisplayName("PUT on /users/test returns OK if the body is valid")
  void updateInfo() throws Exception {

    when(userService.loadUserByUsername(user.getUsername())).thenReturn(user);
    user.setPassword("teeeST2@");

    when(userService.updateUser(eq(user.getUsername()), any(UserDto.class)))
        .thenReturn(modelMapper.map(user, UserDto.class));

    var requestBuilder =
        MockMvcRequestBuilders.put("/users/test")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(jacksonObjectMapper.writeValueAsString(modelMapper.map(user, UserDto.class)));

    mockMvc
        .perform(requestBuilder)
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json(
                    jacksonObjectMapper.writeValueAsString(modelMapper.map(user, UserDto.class))));
  }
}