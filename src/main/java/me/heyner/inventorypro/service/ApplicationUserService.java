package me.heyner.inventorypro.service;

import me.heyner.inventorypro.adapter.ApplicationUserAdapter;
import me.heyner.inventorypro.exception.UserNotFoundException;
import me.heyner.inventorypro.model.ApplicationUser;
import me.heyner.inventorypro.repository.ApplicationUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {

  private final ApplicationUserRepository applicationUserRepository;

  private static final Logger logger = LoggerFactory.getLogger(ApplicationUserService.class);

  private final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

  public ApplicationUserService(ApplicationUserRepository applicationUserRepository) {
    this.applicationUserRepository = applicationUserRepository;
  }

  public ApplicationUser saveApplicationUser(ApplicationUser applicationUser) {
    ApplicationUser newApplicationUser = applicationUserRepository.save(applicationUser);
    logger.info("Saved user: {}", newApplicationUser);
    return newApplicationUser;
  }

  public ApplicationUser getApplicationUser(String username) throws UserNotFoundException {
    ApplicationUser applicationUser =
        applicationUserRepository
            .findUserByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("Not found"));

    logger.info("Found user: {}", applicationUser);

    return applicationUser;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
    ApplicationUser applicationUser = getApplicationUser(username);
    return new ApplicationUserAdapter(applicationUser);
  }
}
