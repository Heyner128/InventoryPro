import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OptionComponent } from './option.component';

import { Option } from '../../../model/option';
import { input, InputSignal, signal } from '@angular/core';

const MOCK_OPTION: Option = {
  name: "Color",
  values: ["Red", "Green", "Blue"],
}

describe('OptionComponent', () => {
  let component: OptionComponent;
  let fixture: ComponentFixture<OptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OptionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OptionComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('option', MOCK_OPTION);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
