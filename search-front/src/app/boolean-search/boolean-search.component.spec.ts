import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BooleanSearchComponent } from './boolean-search.component';

describe('BooleanSearchComponent', () => {
  let component: BooleanSearchComponent;
  let fixture: ComponentFixture<BooleanSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BooleanSearchComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BooleanSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
