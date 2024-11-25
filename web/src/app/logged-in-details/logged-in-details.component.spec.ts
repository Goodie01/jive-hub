import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoggedInDetailsComponent } from './logged-in-details.component';

describe('LoggedInDetailsComponent', () => {
  let component: LoggedInDetailsComponent;
  let fixture: ComponentFixture<LoggedInDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoggedInDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoggedInDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
