import { TestBed } from '@angular/core/testing';

import { OptionsService } from './options.service';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { Option } from '../model/option';
import { AuthenticationService } from './authentication.service';
import { ApiTesting } from '../../testing/api';

const MOCK_DATE = new Date(Date.UTC(2022, 1, 1));

const MOCK_OPTIONS: Option[] = [
  {
    id: '1',
    name: 'Color',
    values: ['Red', 'Blue', 'Green'],
    createdAt: MOCK_DATE,
    updatedAt: MOCK_DATE,
  },
  {
    id: '2',
    name: 'Size',
    values: ['Small', 'Medium', 'Large'],
    createdAt: MOCK_DATE,
    updatedAt: MOCK_DATE,
  },
];

const MOCK_PRODUCT_UUID = 'product-123';

describe('OptionsService', () => {
  let service: OptionsService;
  let authenticationService: AuthenticationService;
  let apiTesting: ApiTesting;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ]
    });
    injectDependencies();
    mockAuthenticationService();
  });

  function injectDependencies() {
    service = TestBed.inject(OptionsService);
    authenticationService = TestBed.inject(AuthenticationService);
    apiTesting = TestBed.inject(ApiTesting);
  }

  function mockAuthenticationService() {
    spyOn(authenticationService, "getUsername").and.returnValue("test_user");
  }


  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get options for a product', () => {
    service.getOptions(MOCK_PRODUCT_UUID).subscribe(options => {
      expect(options).toEqual(MOCK_OPTIONS);
    });
    apiTesting.expectSuccessfulApiResponse({
      body: MOCK_OPTIONS,
    });
  });

  it('should return an error if the API response is not ok', () => {
    service.getOptions(MOCK_PRODUCT_UUID).subscribe({
      next: () => fail('Expected an error, but got options'),
      error: (error) => {
        expect(error).toBeTruthy();
      }
    });

    apiTesting.expectUnsuccessfulApiResponse();
  });

  it('should get a specific option for a product', () => {
    service
      .getOption(MOCK_PRODUCT_UUID, MOCK_OPTIONS[0].id!)
      .subscribe((option) => {
        expect(option).toEqual(MOCK_OPTIONS[0]);
      });
    apiTesting.expectSuccessfulApiResponse({
      body: MOCK_OPTIONS[0],
    });
  });

  it('should return an error if the API response for a specific option is not ok', () => {
    service
      .getOption(MOCK_PRODUCT_UUID, MOCK_OPTIONS[0].id!)
      .subscribe({
        next: () => fail('Expected an error, but got an option'),
        error: (error) => {
          expect(error).toBeTruthy();
        }
      });

    apiTesting.expectUnsuccessfulApiResponse();
  });

  it('should create a new option for a product', () => {
    const newOption = { name: 'Material' };
    service.createOption(MOCK_PRODUCT_UUID, newOption).subscribe((option) => {
      expect(option.name).toEqual(newOption.name);
      expect(option.id).toBeDefined();
    });
    apiTesting.expectSuccessfulApiResponse({
      body: { ...newOption, id: '3', createdAt: MOCK_DATE, updatedAt: MOCK_DATE },
    });
  });

  it('should return an error if the API response for creating an option is not ok', () => {
    const newOption = { name: 'Material' };
    service.createOption(MOCK_PRODUCT_UUID, newOption).subscribe({
      next: () => fail('Expected an error, but got an option'),
      error: (error) => {
        expect(error).toBeTruthy();
      }
    });

    apiTesting.expectUnsuccessfulApiResponse();
  });

  it('should update an existing option for a product', () => {
    const updatedOption = { name: 'Updated Color' };
    service.updateOption(MOCK_PRODUCT_UUID, MOCK_OPTIONS[0].id!, updatedOption).subscribe((option) => {
      expect(option.name).toEqual(updatedOption.name);
      expect(option.id).toEqual(MOCK_OPTIONS[0].id);
    });
    apiTesting.expectSuccessfulApiResponse({
      body: { ...MOCK_OPTIONS[0], ...updatedOption, updatedAt: MOCK_DATE },
    });
  });

  it('should return an error if the API response for updating an option is not ok', () => {
    const updatedOption = { name: 'Updated Color' };
    service.updateOption(MOCK_PRODUCT_UUID, MOCK_OPTIONS[0].id!, updatedOption).subscribe({
      next: () => fail('Expected an error, but got an option'),
      error: (error) => {
        expect(error).toBeTruthy();
      }
    });

    apiTesting.expectUnsuccessfulApiResponse();
  });

  it('should delete an option for a product', () => {
    service.deleteOption(MOCK_PRODUCT_UUID, MOCK_OPTIONS[0].id!).subscribe(() => {
      expect(true).toBeTrue(); // Just checking if the delete call was successful
    }
    );
    apiTesting.expectSuccessfulApiResponse({
      body: MOCK_OPTIONS[0],
    });
  });

  it('should return an error if the API response for deleting an option is not ok', () => {
    service.deleteOption(MOCK_PRODUCT_UUID, MOCK_OPTIONS[0].id!).subscribe({
      next: () => fail('Expected an error, but got a successful delete'),
      error: (error) => {
        expect(error).toBeTruthy();
      }
    });

    apiTesting.expectUnsuccessfulApiResponse();
  });
});
