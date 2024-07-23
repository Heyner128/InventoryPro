package me.heyner.inventorypro.controller;

import jakarta.validation.Valid;
import java.util.List;
import me.heyner.inventorypro.dto.OptionDto;
import me.heyner.inventorypro.model.Option;
import me.heyner.inventorypro.service.OptionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{username}/products/{productIndex}/options")
public class OptionController {

  private final OptionService optionService;

  public OptionController(OptionService optionService) {
    this.optionService = optionService;
  }

  @GetMapping
  public List<Option> getProductOptions(
      @PathVariable String username, @PathVariable int productIndex) {
    return optionService.getOptions(username, productIndex);
  }

  @PostMapping
  public Option createOption(
      @PathVariable String username,
      @PathVariable int productIndex,
      @RequestBody @Valid OptionDto optionDto) {
    return optionService.addOption(username, productIndex, optionDto);
  }

  @GetMapping("/{optionIndex}")
  public Option getOption(
      @PathVariable String username,
      @PathVariable int optionIndex,
      @PathVariable int productIndex) {
    return optionService.getOption(username, productIndex, optionIndex);
  }

  @PutMapping("/{optionIndex}")
  public Option updateOption(
      @PathVariable String username,
      @PathVariable int productIndex,
      @PathVariable int optionIndex,
      @RequestBody @Valid OptionDto optionDto) {
    return optionService.updateOption(username, productIndex, optionIndex, optionDto);
  }

  @DeleteMapping("/{optionIndex}")
  public void deleteOption(
      @PathVariable String username,
      @PathVariable int productIndex,
      @PathVariable int optionIndex) {
    optionService.removeOption(username, productIndex, optionIndex);
  }
}
