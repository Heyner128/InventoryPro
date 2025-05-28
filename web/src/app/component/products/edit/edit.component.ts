import { Component, OnInit, signal, WritableSignal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ProductsService } from '../../../service/products.service';
import { Option } from '../../../model/option';
import { OptionComponent } from '../option/option.component';

@Component({
  selector: 'app-edit',
  imports: [ReactiveFormsModule, RouterLink, OptionComponent],
  templateUrl: './edit.component.html',
  styleUrl: './edit.component.scss'
})
export class EditComponent implements OnInit {
  productId: string | undefined;
  productForm = new FormGroup({
    name: new FormControl(''),
    description: new FormControl(''),
    brand: new FormControl(''),
  });
  optionsForm = new FormGroup({
    name: new FormControl('')
  });
  options: WritableSignal<Option[]> = signal([]);
  statusMessage: string | undefined;
  title = 'Edit Product';

  constructor(
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly productsService: ProductsService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.productId = params.get('id') ?? undefined;
      if (!this.productId) return;
      this.productsService
        .getProduct(this.productId)
        .subscribe({
          next: (product) => {
            this.statusMessage = undefined;
            this.title = `Edit Product ${product.name}`;
            this.productForm.patchValue({
              name: product.name,
              description: product.description,
              brand: product.brand,
            });
          },
          error: () => {
            this.router.navigate(['/error'], {
              skipLocationChange: true,
            });
          },
        });
    });
  }
  
  addOption() {
    if(
      !this.optionsForm.value.name ||
      this.options().map(opt => opt.name).includes(this.optionsForm.value.name)
    ) return;
    this.options().push({
      name: this.optionsForm.value.name,
      values: [],
    });
    this.optionsForm.reset(); 
  }

  update(): void {
    if (
      !this.productId ||
      !this.productForm.value.name ||
      !this.productForm.value.description ||
      !this.productForm.value.brand
    ) return;
    this.productsService
      .updateProduct(this.productId, {
        name: this.productForm.value.name,
        description: this.productForm.value.description,
        brand: this.productForm.value.brand,
      })
      .subscribe({
        next: () => {
          this.router.navigate(["/products"]);
        },
        error: (error) => this.statusMessage = error.message,
      });
  }

  delete(): void {
    if (!this.productId) return;
    this.productsService
      .deleteProduct(this.productId)
      .subscribe({
        next: () => {
          this.router.navigate(["/products"]);
        },
        error: (error: Error) => this.statusMessage = error.message,
      });
  }
}
