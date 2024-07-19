package me.heyner.inventorypro.controller;

import jakarta.validation.Valid;
import me.heyner.inventorypro.exception.ConflictingIndexesException;
import me.heyner.inventorypro.model.ApplicationUser;
import me.heyner.inventorypro.service.ApplicationUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class ApplicationUserController {

  private final ApplicationUserService applicationUserService;

  public ApplicationUserController(ApplicationUserService applicationUserService) {
    this.applicationUserService = applicationUserService;
  }

  @PostMapping("/")
  public ApplicationUser register(@RequestBody @Valid ApplicationUser applicationUser) {
    return applicationUserService.saveApplicationUser(applicationUser);
  }

  @GetMapping("/{username}")
  public ApplicationUser getUser(@PathVariable String username) {
    return applicationUserService.getApplicationUser(username);
  }

  @PutMapping("/{username}")
  public ApplicationUser updateUser(
      @PathVariable String username, @RequestBody @Valid ApplicationUser applicationUser)
      throws ConflictingIndexesException {
    if (!username.equals(applicationUser.getUsername())) {
      throw new ConflictingIndexesException();
    }
    return applicationUserService.saveApplicationUser(applicationUser);
  }
}
