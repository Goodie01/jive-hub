import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {HomeResp, PageResp} from "./rest";

@Injectable({
    providedIn: 'root'
})
export class ApiService {
    constructor(private http: HttpClient) {
    }

    pageResponse(url: String): Observable<PageResp> {
        return this.http.get<PageResp>("api/v1/pages/" + url, this.generateHttpOptions());
    }

    homeResponse(): Observable<HomeResp> {
        return this.http.get<HomeResp>("api/v1/home", this.generateHttpOptions());
    }

    private generateHttpOptions() {
        return {
            headers: new HttpHeaders({
                'Content-Type': 'application/json'
            })
        };
    }
}
