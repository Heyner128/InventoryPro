import { TestBed } from '@angular/core/testing';

import { InventoriesService } from './inventories.service';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { User } from '../model/user';
import { Inventory } from '../model/inventory';
import { AuthenticationService } from './authentication.service';
import { errorInterceptor } from '../interceptor/error.interceptor';
import { ApiTesting } from '../../testing/api';

const MOCK_USER: User = {
  username: "test_user",
  email: "test@test.com",
  authorities: ["USER"],
};

const MOCK_DATE = new Date(Date.UTC(2022, 1, 1));

const MOCK_INVENTORIES: Inventory[] = [
  {
    id: "1",
    name: "Inventory 1",
    description: "Description 1",
    items: [],
    user: MOCK_USER,
    createdAt: MOCK_DATE,
    updatedAt: MOCK_DATE,
  },
  {
    id: "2",
    name: "Inventory 2",
    description: "Description 2",
    items: [],
    user: MOCK_USER,
    createdAt: MOCK_DATE,
    updatedAt: MOCK_DATE,
  },
];

describe('InventoriesService', () => {
  let service: InventoriesService;
  let authenticationService: AuthenticationService;
  let apiTesting: ApiTesting;
  

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        InventoriesService,
        AuthenticationService,
        provideHttpClient(withInterceptors([errorInterceptor]), withFetch()),
        provideHttpClientTesting(),
      ],
    });
    injectDependencies();
    mockAuthenticationService();
  });

  function injectDependencies() {
    service = TestBed.inject(InventoriesService);
    authenticationService = TestBed.inject(AuthenticationService);
    apiTesting = TestBed.inject(ApiTesting);
  }

  function mockAuthenticationService() {
    spyOn(authenticationService, "getUsername").and.returnValue(
      MOCK_USER.username
    );
  }

  it('should be created', () => {
    expect(service).toBeTruthy();
  });


  it("should get the inventories if the API response is ok", () => {
    
    service.getInventories().subscribe((inventories: Inventory[]) => {
      expect(inventories).toEqual(MOCK_INVENTORIES);
    });
    apiTesting.expectSuccessfulApiResponse({body: MOCK_INVENTORIES});
  });
  

  it("should return an error if the API response is not ok", () => {
    service.getInventories().subscribe({
      error: (error: Error) => expect(error).toBeDefined()
    });
    apiTesting.expectUnsuccessfulApiResponse();
  });


  it("should get the inventory if the API response is ok", () => {
    service
      .getInventory(MOCK_INVENTORIES[0].id)
      .subscribe((inventory: Inventory) => {
        expect(inventory).toEqual(MOCK_INVENTORIES[0]);
      });
    apiTesting.expectSuccessfulApiResponse({body: MOCK_INVENTORIES[0]});
  });

  it("should return an error if the API response is not ok", () => {
    service
      .getInventory(MOCK_INVENTORIES[0].id)
      .subscribe({
        error: (error: Error) => expect(error).toBeDefined()
      });
    apiTesting.expectUnsuccessfulApiResponse();
  });

  it("should create the inventory if the API response is ok", () => {

    service.createInventory({
      name: MOCK_INVENTORIES[0].name,
      description: MOCK_INVENTORIES[0].description
    }).subscribe((inventory: Inventory) => {
      expect(inventory).toEqual(MOCK_INVENTORIES[0]);
    });
    apiTesting.expectSuccessfulApiResponse({body: MOCK_INVENTORIES[0], status: 201});  
  });

  it("should return an error if the API response is not ok", () => {
    service.createInventory({
      name: MOCK_INVENTORIES[0].name,
      description: MOCK_INVENTORIES[0].description
    }).subscribe({
      error: (error: Error) => expect(error).toBeDefined()
    });
    apiTesting.expectUnsuccessfulApiResponse();
  });

  it("should update the inventory if the API response is ok", () => {
    service.updateInventory(
      MOCK_INVENTORIES[0].id,
      {
        name: MOCK_INVENTORIES[0].name,
        description: MOCK_INVENTORIES[0].description
      }
    ).subscribe(
      (inventory: Inventory) => {
        expect(inventory).toEqual(MOCK_INVENTORIES[0]);
      }
    );
    apiTesting.expectSuccessfulApiResponse({body: MOCK_INVENTORIES[0]});
  });

  it("should return an error if the API response is not ok", () => {
    service.updateInventory(
      MOCK_INVENTORIES[0].id,
      {
        name: MOCK_INVENTORIES[0].name,
        description: MOCK_INVENTORIES[0].description
      }
    ).subscribe({
      error: (error: Error) => expect(error).toBeDefined()
    });
    apiTesting.expectUnsuccessfulApiResponse();
  });


  it("should delete the inventory if the API response is ok", () => {
    service.deleteInventory(MOCK_INVENTORIES[0].id).subscribe(
      () => {
        expect(true).toBeTruthy();
      }
    );
    apiTesting.expectSuccessfulApiResponse({body: MOCK_INVENTORIES[0]});
  });

  it("should return an error if the API response is not ok", () => {
    service.deleteInventory(MOCK_INVENTORIES[0].id).subscribe({
      error: (error: Error) => expect(error).toBeDefined()
    });
    apiTesting.expectUnsuccessfulApiResponse();
  });
});