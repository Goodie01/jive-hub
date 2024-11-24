import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustWebComponent } from './cust-web.component';

describe('CustWebComponent', () => {
  let component: CustWebComponent;
  let fixture: ComponentFixture<CustWebComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustWebComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustWebComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
