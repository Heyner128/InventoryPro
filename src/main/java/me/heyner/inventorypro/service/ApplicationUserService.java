package me.heyner.inventorypro.service;

import me.heyner.inventorypro.adapter.ApplicationUserAdapter;
import me.heyner.inventorypro.model.ApplicationUser;
import me.heyner.inventorypro.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ApplicationUserService implements UserDetailsService {

    private final UserRepository userRepository;

    private Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    public ApplicationUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = userRepository.findUserByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Not found")
        );


        return new ApplicationUserAdapter(applicationUser);
    }
}
