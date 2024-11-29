import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminAboutConfigComponent } from './admin-about-config.component';

describe('AdminAboutConfigComponent', () => {
  let component: AdminAboutConfigComponent;
  let fixture: ComponentFixture<AdminAboutConfigComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminAboutConfigComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminAboutConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
