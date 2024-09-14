package me.heyner.inventorypro.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import me.heyner.inventorypro.dto.LoginUserDto;
import me.heyner.inventorypro.dto.RegisterUserDto;
import me.heyner.inventorypro.dto.UserDto;
import me.heyner.inventorypro.exception.EntityNotFoundException;
import me.heyner.inventorypro.model.Authority;
import me.heyner.inventorypro.model.User;
import me.heyner.inventorypro.repository.UserRepository;
import me.heyner.inventorypro.service.AuthenticationService;
import me.heyner.inventorypro.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootTest
public class UserAuthenticationServiceTests {

  private static final String MOCK_USER = "test";
  private static final String MOCK_EMAIL = "test@test.com";
  private static final String MOCK_PASSWORD = "teeeST1@";
  private static final String FAKE_EMAIL = "fakeemail@fakemeil.com";
  private static final String FAKE_PASSWORD = "fakepassword";

  private final ModelMapper modelMapper = new ModelMapper();
  private final UserService userService;
  private final AuthenticationService authenticationService;
  private User user;
  private LoginUserDto loginUserDto;
  private LoginUserDto invalidLoginUserDto;
  private RegisterUserDto registerUserDto;

  @MockBean private UserRepository userRepository;
  @MockBean private AuthenticationManager authenticationManager;

  @Autowired
  public UserAuthenticationServiceTests(
      UserRepository userRepository,
      UserService userService,
      AuthenticationService authenticationService,
      AuthenticationManager authenticationManager) {
    this.userRepository = userRepository;
    this.userService = userService;
    this.authenticationService = authenticationService;
    this.authenticationManager = authenticationManager;
  }

  void setupMockUser() {
    user = new User();
    user.setId(1L);
    user.setUsername(MOCK_USER);
    user.setPassword(MOCK_PASSWORD);
    user.setEmail(MOCK_EMAIL);
    user.setAuthorities(List.of(Authority.USER));
  }

  void setUpDto() {
    loginUserDto = new LoginUserDto();
    loginUserDto.setUsername(MOCK_USER);
    loginUserDto.setPassword(MOCK_PASSWORD);
    invalidLoginUserDto = new LoginUserDto();
    invalidLoginUserDto.setUsername(MOCK_USER);
    invalidLoginUserDto.setPassword(FAKE_PASSWORD);
    registerUserDto = new RegisterUserDto();
    registerUserDto.setUsername(MOCK_USER);
    registerUserDto.setPassword(MOCK_PASSWORD);
    registerUserDto.setEmail(MOCK_EMAIL);
  }

  void setupMockBeans() {
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(userRepository.findByUsername(loginUserDto.getUsername())).thenReturn(Optional.of(user));
    when(userRepository.findByEmail(UserAuthenticationServiceTests.FAKE_EMAIL))
        .thenThrow(EntityNotFoundException.class);
    when(userRepository.findByUsername(UserAuthenticationServiceTests.FAKE_EMAIL))
        .thenReturn(Optional.empty());
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    when(authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                invalidLoginUserDto.getUsername(), invalidLoginUserDto.getPassword())))
        .thenThrow(BadCredentialsException.class);
  }

  @BeforeEach
  void setUp() {
    setupMockUser();
    setUpDto();
    setupMockBeans();
  }

  @Test
  public void signUpUser() {
    User signedUpUser = modelMapper.map(authenticationService.signUp(registerUserDto), User.class);
    assertNotEquals(signedUpUser.getPassword(), registerUserDto.getPassword());
    assertEquals(signedUpUser.getUsername(), user.getUsername());
    assertEquals(signedUpUser.getEmail(), user.getEmail());
  }

  @Test
  public void authenticateUser() {
    assertThrows(
        BadCredentialsException.class,
        () -> authenticationService.authenticate(invalidLoginUserDto));
    assertEquals(
        authenticationService.authenticate(loginUserDto), modelMapper.map(user, UserDto.class));
  }

  @Test
  public void findOne() {
    UserDto foundUser;
    foundUser = userService.findByEmail(user.getEmail());
    assertEquals(foundUser, modelMapper.map(user, UserDto.class));
    assertThrows(
        EntityNotFoundException.class,
        () -> userService.findByEmail(UserAuthenticationServiceTests.FAKE_EMAIL));
  }

  @Test
  public void findOneByUsername() {
    when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
    User foundUser = userService.loadUserByUsername(user.getUsername());
    assertEquals(foundUser, user);
    assertThrows(
        UsernameNotFoundException.class,
        () -> userService.loadUserByUsername("fakeusername@fakemeil.com"));
  }
}
