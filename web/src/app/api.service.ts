import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {AdminQueryResp, HomeResp, LoginReq, LoginResp, PageResp} from "./rest";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private token?: string = undefined;
  constructor(private http: HttpClient) {
    let newVar = localStorage.getItem("userToken");
    this.token = newVar == null ? undefined : newVar; //Seriously Javascript?
  }

  pageResponse(url: String): Observable<PageResp> {
    return this.http.get<PageResp>("api/v1/pages/" + url, this.generateHttpOptions());
  }

  homeResponse(): Observable<HomeResp> {
    return this.http.get<HomeResp>("api/v1/home", this.generateHttpOptions());
  }

  adminQueryResponse(): Observable<AdminQueryResp> {
    return this.http.get<AdminQueryResp>("api/v1/admin", this.generateHttpOptions());
  }

  login(email: string): Observable<LoginResp> {
    var loginRequest: LoginReq = {
      email: email
    };
    return this.http.post<LoginResp>("api/v1/login", loginRequest, this.generateHttpOptions())
  }

  private generateHttpOptions() {
    if(this.token != undefined) {
      return {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Authorization': this.token
        })
      };
    } else {
      return {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        })
      };
    }
  }

  setToken(token: string) {
    this.token = token;
    localStorage.setItem("userToken", token);
  }

  unsetToken() {
    this.token = undefined;
    localStorage.removeItem("userToken");
  }
}
