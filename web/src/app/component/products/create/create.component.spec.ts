import { TestBed } from '@angular/core/testing';

import { CreateComponent } from './create.component';
import { User } from '../../../model/user';
import { RouterTestingHarness } from '@angular/router/testing';
import { ProductsService } from '../../../service/products.service';
import { provideRouter, Router } from '@angular/router';
import { AuthenticationService } from '../../../service/authentication.service';
import { of, throwError } from 'rxjs';
import { provideHttpClient } from '@angular/common/http';
import { InputTesting } from '../../../../testing/input';

const MOCK_USER: User = {
  username: "test_user",
  email: "test@test.com",
  authorities: ["USER"]
};

const MOCK_DATE = new Date(Date.UTC(2022, 1, 1));

const MOCK_PRODUCT = {
  id: "1",
  name: "Product 1",
  description: "Description 1",
  brand: "Brand 1",
  user: MOCK_USER,
  createdAt: MOCK_DATE,
  updatedAt: MOCK_DATE,
};

describe('CreateComponent', () => {
  let component: CreateComponent;
  let harness: RouterTestingHarness;
  let router: Router;
  let productsService: ProductsService;
  let authenticationService: AuthenticationService;
  let productsSpy: jasmine.Spy;

  let nameInput: HTMLInputElement | null;
  let descriptionInput: HTMLTextAreaElement | null;
  let brandInput: HTMLInputElement | null;
  let submitButton: HTMLButtonElement | null;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateComponent],
      providers: [
        provideRouter([{
          path: '**', component: CreateComponent
        }]),
        provideHttpClient(),
      ]
    })
    .compileComponents();

    injectDependencies();
    stubAuthentication();
    stubProducts();
    await initializeRouter();
    searchForInputs();
    harness.detectChanges();
  });

  function injectDependencies() {
    router = TestBed.inject(Router);
    productsService = TestBed.inject(ProductsService);
    authenticationService = TestBed.inject(AuthenticationService);
  }

  function stubAuthentication() {
    spyOn(authenticationService, 'isAuthenticated').and.returnValue(of(true));
  }

  function stubProducts() {
    productsSpy = spyOn(productsService, 'createProduct').and.returnValue(of(MOCK_PRODUCT));
  }

  function searchForInputs()  {
    nameInput = document.querySelector('input[name="name"]');
    descriptionInput = document.querySelector('textarea[name="description"]');
    brandInput = document.querySelector('input[name="brand"]');
    submitButton = document.querySelector('button.product-form__submit');
  }

  async function initializeRouter() {
    harness = await RouterTestingHarness.create();
    component = await harness.navigateByUrl('/products/create', CreateComponent);
  }

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect to /products if the api response is ok', async () => {
    InputTesting.insertText(nameInput!, MOCK_PRODUCT.name);
    InputTesting.insertText(descriptionInput!, MOCK_PRODUCT.description);
    InputTesting.insertText(brandInput!, MOCK_PRODUCT.brand);

    submitButton!.click();
    await harness.fixture.whenStable();
    harness.detectChanges();
    expect(router.url).toBe('/products');
  });

  it('should show an error message if the api response is not ok', async () => {
    productsSpy.and.returnValue(throwError(() => new Error('Error creating product')));
    InputTesting.insertText(nameInput!, MOCK_PRODUCT.name);
    InputTesting.insertText(descriptionInput!, MOCK_PRODUCT.description);
    InputTesting.insertText(brandInput!, MOCK_PRODUCT.brand);

    submitButton!.click();
    await harness.fixture.whenStable();
    harness.detectChanges();

    expect(harness.routeNativeElement?.textContent).toContain('Error creating product');
  });
});
