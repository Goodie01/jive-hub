import {EventEmitter, Injectable} from '@angular/core';
import {HomeResp} from './rest';
import {ApiService} from './api.service';

@Injectable({
  providedIn: 'root'
})
export class ApiDataCacheService {
  private requestingHomeResp: boolean = false;
  private homeRespData?: HomeResp = undefined
  private homeRespEventEmitter: EventEmitter<HomeResp> = new EventEmitter()

  constructor(private apiService: ApiService) { }

  refreshHomeResp() {
    if(!this.requestingHomeResp) {
      this.requestingHomeResp = true;
      this.apiService.homeResponse()
        .subscribe(value => {
          this.homeRespData = value;
          this.homeRespEventEmitter.emit(value);
          this.requestingHomeResp = false;
        });
    }
  }

  public homeResp(next?: (value: HomeResp) => void) {
    if(next == undefined) {
      return;
    }
    this.homeRespEventEmitter.subscribe(next)
    if(this.homeRespData != undefined) {
      next(this.homeRespData)
    } else {
      this.refreshHomeResp();
    }
  }
}
