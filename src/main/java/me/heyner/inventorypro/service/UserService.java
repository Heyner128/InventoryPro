package me.heyner.inventorypro.service;

import me.heyner.inventorypro.dto.UserDto;
import me.heyner.inventorypro.exception.EntityNotFoundException;
import me.heyner.inventorypro.model.User;
import me.heyner.inventorypro.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);
  private final UserRepository userRepository;
  private final ModelMapper modelMapper = new ModelMapper();

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User findByEmail(String email) throws EntityNotFoundException {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

    logger.info("Found user: {}", user);
    return user;
  }

  public UserDto updateUser(String username, UserDto userDto) {
    // TODO - i can't update passwords :(
    User user = loadUserByUsername(username);
    user.setUsername(userDto.getUsername());
    user.setEmail(userDto.getEmail());
    user.setAuthorities(userDto.getAuthorities());
    User savedUser = userRepository.save(user);
    logger.info("Updated user: {}", user);
    return modelMapper.map(savedUser, UserDto.class);
  }

  @Override
  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Not found"));

    logger.info("Found user: {}", user);
    return user;
  }
}
