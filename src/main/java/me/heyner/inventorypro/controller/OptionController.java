package me.heyner.inventorypro.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import me.heyner.inventorypro.dto.OptionDto;
import me.heyner.inventorypro.service.OptionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{username}/products/{productUuid}/options")
public class OptionController {

  private final OptionService optionService;

  public OptionController(OptionService optionService) {
    this.optionService = optionService;
  }

  @GetMapping
  public List<OptionDto> getProductOptions(@PathVariable UUID productUuid) {
    return optionService.getOptions(productUuid);
  }

  @PostMapping
  public OptionDto createOption(
      @PathVariable UUID productUuid, @RequestBody @Valid OptionDto optionDto) {
    return optionService.addOption(productUuid, optionDto);
  }

  @PutMapping()
  public List<OptionDto> updateOptions(
      @PathVariable UUID productUuid, @RequestBody @Valid List<OptionDto> optionDtos) {
    return optionService.updateOptions(productUuid, optionDtos);
  }
}
