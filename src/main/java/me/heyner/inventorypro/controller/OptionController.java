package me.heyner.inventorypro.controller;

import jakarta.validation.Valid;
import me.heyner.inventorypro.exception.ConflictingIndexesException;
import me.heyner.inventorypro.model.OptionValue;
import me.heyner.inventorypro.service.OptionService;
import me.heyner.inventorypro.model.Option;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OptionController {

  private final OptionService optionService;

  public OptionController(OptionService optionService) {
    this.optionService = optionService;
  }

  @GetMapping("/products/{productId}/options")
  public List<Option> getProductOptions(@PathVariable Long productId) {
    return optionService.getOptionsByProductId(productId);
  }

  @PostMapping("/products/{productId}/options")
  public Option createOption(@PathVariable Long productId, @RequestBody @Valid Option option) {
    return optionService.addOption(productId, option);
  }

  @GetMapping("/products/{productId}/options/{optionIndex}")
  public Option getOption(@PathVariable Long productId, @PathVariable int optionIndex) {
    return optionService.getOption(productId, optionIndex);
  }

  @PutMapping("/products/{productId}/options/{optionIndex}")
  public Option updateOption(
      @PathVariable Long productId,
      @PathVariable int optionIndex,
      @RequestBody @Valid Option option) {
    if (optionIndex != option.getIndex()) throw new ConflictingIndexesException();
    return optionService.updateOption(productId, option);
  }

  @DeleteMapping("/products/{productId}/options/{optionIndex}")
  public void deleteOption(@PathVariable Long productId, @PathVariable int optionIndex) {
    optionService.removeOption(productId, optionIndex);
  }

  @GetMapping("/products/{productId}/options/{optionIndex}/values")
  public List<OptionValue> getOptionValues(
      @PathVariable Long productId, @PathVariable int optionIndex) {
    return optionService.getOption(productId, optionIndex).getValues();
  }

  @PostMapping("/products/{productId}/options/{optionIndex}/values")
  public OptionValue addOptionValue(
      @PathVariable Long productId,
      @PathVariable int optionIndex,
      @RequestBody @Valid OptionValue optionValue) {
    return optionService.addValue(productId, optionIndex, optionValue);
  }

  @GetMapping("products/{productId}/options/{optionIndex}/values/{optionValueIndex}")
  public OptionValue getOptionValue(
      @PathVariable Long productId,
      @PathVariable int optionIndex,
      @PathVariable int optionValueIndex) {
    return optionService.getOptionValue(productId, optionIndex, optionValueIndex);
  }

  @PutMapping("products/{productId}/options/{optionIndex}/values/{optionValueIndex}")
  public OptionValue updateOptionValue(
      @PathVariable Long productId,
      @PathVariable int optionIndex,
      @PathVariable int optionValueIndex,
      @RequestBody @Valid OptionValue optionValue) {
    if (optionValueIndex != optionValue.getIndex()) throw new ConflictingIndexesException();
    return optionService.updateOptionValue(productId, optionIndex, optionValue);
  }

  @DeleteMapping("products/{productId}/options/{optionIndex}/values/{optionValueIndex}")
  public void deleteOptionValue(
      @PathVariable Long productId,
      @PathVariable int optionIndex,
      @PathVariable int optionValueIndex) {
    optionService.removeOptionValue(productId, optionIndex, optionValueIndex);
  }
}
