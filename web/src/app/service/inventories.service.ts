import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthenticationService } from './authentication.service';
import { environment } from '../../environments/environment';
import { catchError, map, Observable, of } from 'rxjs';
import type { Inventory } from '../model/inventory';

@Injectable({
  providedIn: 'root'
})
export class InventoriesService {

  constructor(
    private readonly httpClient: HttpClient,
    private readonly authenticationService: AuthenticationService
  ) {}

  getInventories(): Observable<HttpResponse<Inventory[]>> {
    return this.httpClient.get<HttpResponse<Inventory[]>>(
      `${environment.apiBaseUrl}/users/${this.authenticationService.getUsername()}/inventory`,
      {
        observe: 'response'
      }
    )
    .pipe(
      catchError((errorResponse) => of(errorResponse)),
      map(response => {
        if(response.ok && response.body !== null) {
          response.body = response.body.map((inventory: Inventory) => ({
            id: inventory.id,
            name: inventory.name,
            createdDate: new Date(inventory.createdDate),
            updateDate: new Date(inventory.updateDate)
          }));
        }
        return response;
      })
    )
  }
}
