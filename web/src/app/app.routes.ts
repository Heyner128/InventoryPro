import { Routes } from '@angular/router';   
import { LoginComponent } from './component/login/login.component';
import { LayoutComponent } from './component/layout/layout.component';
import { loginGuard } from './guard/login.guard';
import { InventoriesComponent } from './component/inventories/inventories.component';
import { CreateComponent as InventoriesCreateComponent } from './component/inventories/create/create.component';
import { EditComponent as InventoriesDetailsComponent } from './component/inventories/edit/edit.component';
import { HomeComponent } from './component/home/home.component';
import { SignupComponent } from './component/signup/signup.component';
import { ErrorComponent } from './component/error/error.component';
import { ProductsComponent } from './component/products/products.component';
import { CreateComponent as ProductsCreateComponent } from './component/products/create/create.component';
import { EditComponent as ProductsEditComponent } from './component/products/edit/edit.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'signup',
    component: SignupComponent,
  },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [loginGuard],
    children: [
      {
        path: '',
        component: HomeComponent,
      },
      {
        path: 'inventories',
        component: InventoriesComponent,
      },
      {
        path: 'inventories/create',
        component: InventoriesCreateComponent,
      },
      {
        path: 'inventories/:id',
        component: InventoriesDetailsComponent,
      },
      {
        path: 'products',
        component: ProductsComponent,
      },
      {
        path: 'products/create',
        component: ProductsCreateComponent
      },
      {
        path: 'products/:id',
        component: ProductsEditComponent
      },
      {
        path: 'error',
        component: ErrorComponent,
      }
    ],
  },
];
