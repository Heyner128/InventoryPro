import { TestBed } from '@angular/core/testing';

import { CreateComponent } from './create.component';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter, Router } from '@angular/router';
import { User } from '../../../model/user';
import { Inventory } from '../../../model/inventory';
import { RouterTestingHarness } from '@angular/router/testing';
import { InventoriesService } from '../../../service/inventories.service';
import { AuthenticationService } from '../../../service/authentication.service';
import { of, throwError } from 'rxjs';
import { InputTesting } from '../../../../testing/input';

const MOCK_USER: User = {
  username: "test_user",
  email: "test@test.com",
  authorities: ["USER"],
};

const MOCK_DATE = new Date(Date.UTC(2022, 1, 1));

const MOCK_INVENTORY: Inventory = {
  id: "1",
  name: "Inventory 1",
  description: "Description 1",
  items: [],
  user: MOCK_USER,
  createdAt: MOCK_DATE,
  updatedAt: MOCK_DATE,
};

describe('CreateComponent', () => {
  let component: CreateComponent;
  let harness: RouterTestingHarness;
  let router: Router;
  let inventoriesService: InventoriesService;
  let authenticationService: AuthenticationService;
  let inventoriesSpy: jasmine.Spy;

  let nameInput: HTMLInputElement | null;
  let descriptionInput: HTMLTextAreaElement | null;
  let submitButton: HTMLButtonElement | null;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateComponent],
      providers: [
        provideHttpClient(),
        provideRouter([
          {
            path: "**",
            component: CreateComponent,
          },
        ]),
      ],
    }).compileComponents();
    injectDependencies();
    stubAuthentication();
    stubInventories();
    
    await initializeRouter();
    searchForInputs();
    harness.detectChanges();
  });

  function injectDependencies() {
    router = TestBed.inject(Router);
    inventoriesService = TestBed.inject(InventoriesService);
    authenticationService = TestBed.inject(AuthenticationService);
  }

  function stubAuthentication() {
    spyOn(authenticationService, "isAuthenticated").and.returnValue(of(true));
  }

  function stubInventories() {
    inventoriesSpy = spyOn(inventoriesService, "createInventory").and.returnValue(of(MOCK_INVENTORY));
  }

  async function initializeRouter() {
    harness = await RouterTestingHarness.create();
    component = await harness.navigateByUrl("/inventories", CreateComponent);
  }

  function searchForInputs() {
    nameInput = harness.routeNativeElement!.querySelector("input[name='name']");
    descriptionInput = harness.routeNativeElement!.querySelector(
      "textarea[name='description']"
    );
    submitButton = harness.routeNativeElement!.querySelector(
      "button[type='submit']"
    );
  }


  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect to /inventories if the inventory is created successfully', async () => {
    InputTesting.insertText(nameInput!, "Inventory 1");
    InputTesting.insertText(descriptionInput!, "Description 1");

    submitButton?.click();
    await harness.fixture.whenStable();
    harness.detectChanges();

    expect(router.url).toBe("/inventories");
  });

  it('should show an error message if the inventory creation fails', async () => {
    inventoriesSpy.and.returnValue(throwError(() => new Error("Failed to create inventory")));
    InputTesting.insertText(nameInput!, "Inventory 1");
    InputTesting.insertText(descriptionInput!, "Description 1");

    submitButton?.click();
    await harness.fixture.whenStable();
    harness.detectChanges();

    expect(harness.routeNativeElement?.textContent).toContain("Failed to create inventory");
  });
});
