package me.heyner.inventorypro.service;

import me.heyner.inventorypro.exception.UserNotFoundException;
import me.heyner.inventorypro.model.User;
import me.heyner.inventorypro.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User saveApplicationUser(User user) {
    User newUser = userRepository.save(user);
    logger.info("Saved user: {}", newUser);
    return newUser;
  }

  @Override
  public User loadUserByUsername(String username) throws UserNotFoundException {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("Not found"));

    logger.info("Found user: {}", user);
    return user;
  }
}
