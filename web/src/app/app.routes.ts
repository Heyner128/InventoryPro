import { Routes } from '@angular/router';   
import { LoginComponent } from './component/login/login.component';
import { LayoutComponent } from './component/layout/layout.component';
import { loginGuard } from './guard/login.guard';
import { InventoriesComponent } from './component/inventories/inventories.component';
import { HomeComponent } from './component/home/home.component';

export const routes: Routes = [
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: '',
        component: LayoutComponent,
        canActivate: [loginGuard],
        children: [
            {
                path: '',
                component: HomeComponent
            },
            {
                path: 'inventories',
                component: InventoriesComponent
            }
        ]
    }
];
