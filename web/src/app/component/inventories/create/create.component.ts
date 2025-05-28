import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { InventoriesService } from '../../../service/inventories.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-create',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './create.component.html',
  styleUrl: './create.component.scss'
})
export class CreateComponent {

  inventoryForm = new FormGroup({
    name: new FormControl(''),
    description: new FormControl(''),
  });

  statusMessage: string | undefined;

  constructor(
    private readonly inventoriesService: InventoriesService,
    private readonly router: Router
  ) {
  }

  create() {
    if (!this.inventoryForm.value.name) return;
    this.inventoriesService
      .createInventory({
        name: this.inventoryForm.value.name,
        description: this.inventoryForm.value.description ?? ""
      })
      .subscribe({
        next: () => {
          this.router.navigate(["/inventories"]);
        },
        error: (message) => (this.statusMessage = message),
      });
  }

}
