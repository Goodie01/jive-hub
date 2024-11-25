import { TestBed } from '@angular/core/testing';

import { ApiDataCacheService } from './api-data-cache.service';

describe('ApiDataCacheService', () => {
  let service: ApiDataCacheService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApiDataCacheService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
