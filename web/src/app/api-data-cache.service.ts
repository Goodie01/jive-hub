import {EventEmitter, Injectable} from '@angular/core';
import {AdminQueryResp, HomeResp} from './rest';
import {ApiService} from './api.service';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiDataCacheService {
  public homeResponse: CachedApiThing<HomeResp>;
  public adminQueryResp: CachedApiThing<AdminQueryResp>;

  constructor(private apiService: ApiService) {
    this.homeResponse = new CachedApiThing(() => apiService.homeResponse())
    this.adminQueryResp = new CachedApiThing(() => apiService.adminQueryResponse())
  }
}

class CachedApiThing<Type> {
  private requesting: boolean = false;
  private data?: Type = undefined;
  private eventEmitter: EventEmitter<Type> = new EventEmitter();


  constructor(private supplier: () => Observable<Type>) {
  }

  refresh() {
    if(!this.requesting) {
      this.requesting = true;
      this.supplier().subscribe(value => {
        this.data = value;
        this.eventEmitter.emit(value);
        this.requesting = false;
        })
    }
  }

  subscribe(next?: (value: Type) => void) {
    if (next == undefined) {
      return;
    }
    this.eventEmitter.subscribe(next)
    if (this.data != undefined) {
      next(this.data)
    } else {
      this.refresh();
    }
  }
}
