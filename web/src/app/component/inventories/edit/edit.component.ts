import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { InventoriesService } from '../../../service/inventories.service';
import { Inventory } from '../../../model/inventory';
import { catchError } from 'rxjs/operators';
import { EMPTY } from 'rxjs';

@Component({
  selector: 'app-details',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './edit.component.html',
  styleUrl: './edit.component.scss'
})
export class EditComponent implements OnInit {
  inventoryId: string | undefined;
  inventoryForm = new FormGroup({
    name: new FormControl(''),
    description: new FormControl(''),
  });
  statusMessage: string | undefined;
  title = 'Edit Inventory';

  constructor(
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly inventoriesService: InventoriesService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.inventoryId = params.get('id') ?? undefined;
      if (!this.inventoryId) return;
      this.inventoriesService
        .getInventory(this.inventoryId)
        .subscribe({
          next: (inventory: Inventory) => {
            this.statusMessage = undefined;
            this.title = `Edit Inventory ${inventory.name}`;
            this.inventoryForm.patchValue({
              name: inventory.name,
              description: inventory.description,
            });
          },
          error: () => {
            this.router.navigate(["/error"], {
              skipLocationChange: true,
            })
          },
        });
    });
  }

  update(): void {
    if (!this.inventoryId || !this.inventoryForm.value.name) return;
    this.inventoriesService
      .updateInventory(
        this.inventoryId,
        {
          name: this.inventoryForm.value.name,
          description: this.inventoryForm.value.description ?? undefined
        }
      )
      .subscribe({
        next: () => {
          this.router.navigate(["/inventories"]);
        },
        error: (error: Error) => this.statusMessage = error.message,
      });
  }

  delete(): void {
    if (!this.inventoryId) return;
    this.inventoriesService
      .deleteInventory(this.inventoryId)
      .subscribe({
        next: () => {
          this.router.navigate(["/inventories"]);
        },
        error: (error: Error) => this.statusMessage = error.message,
      });
  }
}
