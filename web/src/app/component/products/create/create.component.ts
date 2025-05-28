import { Component, signal, WritableSignal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ProductsService } from '../../../service/products.service';
import { Option } from '../../../model/option';
import { Router, RouterLink } from '@angular/router';
import { OptionComponent } from '../option/option.component';

@Component({
  selector: "app-create",
  imports: [ReactiveFormsModule, RouterLink, OptionComponent],
  templateUrl: "./create.component.html",
  styleUrl: "./create.component.scss",
})
export class CreateComponent {
  productForm = new FormGroup({
    name: new FormControl(""),
    description: new FormControl(""),
    brand: new FormControl(""),
  });
  optionsForm = new FormGroup({
    name: new FormControl(""),
  });
  options: WritableSignal<Option[]> = signal([]);
  statusMessage?: string;

  constructor(
    private readonly productsService: ProductsService,
    private readonly router: Router
  ) {}

  create() {
    if (!this.productForm.value.name) return;
    this.productsService
      .createProduct({
        name: this.productForm.value.name,
        description: this.productForm.value.description ?? "",
        brand: this.productForm.value.brand ?? "",
      })
      .subscribe({
        next: () => {
          this.router.navigateByUrl("/products");
        },
        error: (message) => (this.statusMessage = message),
      });
  }

  addOption() {
    if (
      !this.optionsForm.value.name ||
      this.options()
        .map((opt) => opt.name)
        .includes(this.optionsForm.value.name)
    )
      return;
    this.options().push({
      name: this.optionsForm.value.name,
      values: [],
    });
    this.optionsForm.reset(); 
  }
}
