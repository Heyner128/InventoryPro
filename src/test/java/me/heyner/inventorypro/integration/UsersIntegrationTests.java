package me.heyner.inventorypro.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Map;
import me.heyner.inventorypro.dto.LoginUserDto;
import me.heyner.inventorypro.dto.RegisterUserDto;
import me.heyner.inventorypro.dto.UserDto;
import me.heyner.inventorypro.model.Authority;
import me.heyner.inventorypro.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UsersIntegrationTests {

  @Autowired TestRestTemplate restTemplate;
  @Autowired UserRepository userRepository;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  public ResponseEntity<UserDto> registerRequest() {
    RegisterUserDto registerUserDto =
        new RegisterUserDto().setEmail("test@test.com").setUsername("test").setPassword("teeeST@1");

    return restTemplate.postForEntity("/users", registerUserDto, UserDto.class);
  }

  public ResponseEntity<Map> loginRequest() {
    LoginUserDto loginUserDto = new LoginUserDto().setUsername("test").setPassword("teeeST@1");

    return restTemplate.postForEntity("/users/login", loginUserDto, Map.class);
  }

  @Test
  @DisplayName(
      "POST to /users with email, username and password returns OK and the user information ")
  public void registerUser() {

    var response = registerRequest();

    assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
    assertThat(response.getBody().getUsername(), equalTo("test"));
    assertThat(response.getBody().getEmail(), equalTo("test@test.com"));
  }

  @Test
  @DisplayName("POST to /login with email and password logs in the user")
  public void loginUser() {
    // Registers a new user
    registerUser();

    // Tests the login
    var response = loginRequest();

    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    assertThat(response.getBody().get("token"), notNullValue());
    assertThat(response.getBody().get("username"), equalTo("test"));
  }

  @Test
  public void getUserDetails() {

    // Registers a new user
    registerRequest();

    // Logs in the user
    var loginResponse = loginRequest();

    // creates an authenticated request
    RequestEntity<Void> request =
        RequestEntity.get("/users/test")
            .header("Authorization", "Bearer " + loginResponse.getBody().get("token"))
            .build();

    // Gets the created user information
    ResponseEntity<UserDto> response = restTemplate.exchange(request, UserDto.class);

    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    assertThat(response.getBody().getUsername(), equalTo("test"));
    assertThat(response.getBody().getEmail(), equalTo("test@test.com"));
  }

  @Test
  public void updateUserDetails() {

    // Registers a new user
    registerRequest();

    // Logs in the user
    var loginResponse = loginRequest();

    // creates an authenticated request
    RequestEntity<UserDto> request =
        RequestEntity.put("/users/test")
            .header("Authorization", "Bearer " + loginResponse.getBody().get("token"))
            .body(
                new UserDto()
                    .setUsername("test2")
                    .setEmail("test@test.com")
                    .setAuthorities(List.of(Authority.USER)));

    // Gets the created user information
    ResponseEntity<UserDto> response = restTemplate.exchange(request, UserDto.class);

    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    assertThat(response.getBody().getUsername(), equalTo("test2"));
    assertThat(response.getBody().getEmail(), equalTo("test@test.com"));
  }
}