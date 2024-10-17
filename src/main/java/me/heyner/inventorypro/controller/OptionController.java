package me.heyner.inventorypro.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import me.heyner.inventorypro.dto.OptionInputDto;
import me.heyner.inventorypro.model.Option;
import me.heyner.inventorypro.service.OptionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{username}/products/{productUuid}/options")
public class OptionController {

  private final OptionService optionService;

  public OptionController(OptionService optionService) {
    this.optionService = optionService;
  }

  @GetMapping
  public List<Option> getProductOptions(@PathVariable UUID productUuid) {
    return optionService.getOptions(productUuid);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Option createOption(
      @PathVariable UUID productUuid, @RequestBody @Valid OptionInputDto optionDto) {
    return optionService.addOption(productUuid, optionDto);
  }

  @PutMapping()
  public List<Option> updateOptions(
      @PathVariable UUID productUuid, @RequestBody @Valid List<OptionInputDto> optionDtos) {
    return optionService.updateOptions(productUuid, optionDtos);
  }
}
