import { TestBed } from '@angular/core/testing';

import { EditComponent } from './edit.component';
import { RouterTestingHarness } from '@angular/router/testing';
import { provideRouter, Router } from '@angular/router';
import { ProductsService } from '../../../service/products.service';
import { AuthenticationService } from '../../../service/authentication.service';
import { of, throwError } from 'rxjs';
import { User } from '../../../model/user';
import { provideHttpClient } from '@angular/common/http';
import { ProductsComponent } from '../products.component';
import { ErrorComponent } from '../../error/error.component';
import { InputTesting } from '../../../../testing/input';

const MOCK_USER: User = {
  username: "test_user",
  email: "test@test.com",
  authorities: ["USER"],
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

describe('EditComponent', () => {
  let component: EditComponent;
  let harness: RouterTestingHarness;
  let router: Router;
  let productsService: ProductsService;
  let authenticationService: AuthenticationService;
  let getProductSpy: jasmine.Spy;
  let updateProductSpy: jasmine.Spy;
  let deleteProductSpy: jasmine.Spy;

  let nameInput: HTMLInputElement | null;
  let descriptionInput: HTMLTextAreaElement | null;
  let brandInput: HTMLInputElement | null;
  let submitButton: HTMLButtonElement | null;
  let deleteButton: HTMLButtonElement | null;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditComponent],
      providers: [
        provideRouter([
          {path: "products/:id", component: EditComponent},
          {path: "products", component: ProductsComponent},
          {path: "error", component: ErrorComponent},
        ]),
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
    updateProductSpy = spyOn(productsService, 'updateProduct').and.returnValue(of(MOCK_PRODUCT));
    getProductSpy = spyOn(productsService, 'getProduct').and.returnValue(of(MOCK_PRODUCT));
    deleteProductSpy = spyOn(productsService, 'deleteProduct').and.returnValue(of({}));
  }

  async function initializeRouter() {
    harness = await RouterTestingHarness.create();
    component = await harness.navigateByUrl(`/products/${MOCK_PRODUCT.id}`, EditComponent);
  }

  function searchForInputs() {
    nameInput = harness.routeNativeElement!.querySelector('input[name="name"]');
    descriptionInput = harness.routeNativeElement!.querySelector('textarea[name="description"]');
    brandInput = harness.routeNativeElement!.querySelector('input[name="brand"]');
    submitButton = harness.routeNativeElement!.querySelector('button.product-form__submit');
    deleteButton = harness.routeNativeElement!.querySelector('button.product-form__delete');
  }

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with product data', () => {
    expect(nameInput?.value).toBe(MOCK_PRODUCT.name);
    expect(descriptionInput?.value).toBe(MOCK_PRODUCT.description);
    expect(brandInput?.value).toBe(MOCK_PRODUCT.brand);
  });

  it('should redirect to /products if the update is successful', async () => {
    InputTesting.insertText(nameInput!, 'Updated Product');
    InputTesting.insertText(descriptionInput!, 'Updated Description');
    InputTesting.insertText(brandInput!, 'Updated Brand');
    submitButton!.click();

    await harness.fixture.whenStable();
    expect(router.url).toBe('/products');
  });

  it('should show an error message if the update fails', async () => {
    const errorMessage = 'Update failed'; 
    updateProductSpy.and.returnValue(throwError(() => new Error(errorMessage)));
    InputTesting.insertText(nameInput!, 'Updated Product');
    InputTesting.insertText(descriptionInput!, 'Updated Description');
    InputTesting.insertText(brandInput!, 'Updated Brand');
    submitButton!.click();

    await harness.fixture.whenStable();
    harness.detectChanges();

    expect(harness.routeNativeElement?.textContent).toContain(errorMessage);
  });

  it('should delete the product and redirect to /products', async () => {
    deleteButton!.click();
    await harness.fixture.whenStable();
    expect(router.url).toBe('/products');
  });

  it('should show an error message if the delete fails', async () => {
    const errorMessage = 'Delete failed';
    deleteProductSpy.and.returnValue(throwError(() => new Error(errorMessage)));
    deleteButton!.click();

    await harness.fixture.whenStable();
    harness.detectChanges();

    expect(harness.routeNativeElement?.textContent).toContain(errorMessage);
  });

  it('should redirect to error page if the product is not found', async () => {
    getProductSpy.and.returnValue(throwError(() => new Error('Product not found')));
    component.ngOnInit();
    await harness.fixture.whenStable();
    harness.detectChanges();
    expect(router.url).toBe('/error');
  });
});
