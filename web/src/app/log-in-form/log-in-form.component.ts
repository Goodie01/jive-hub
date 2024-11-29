import {Component} from '@angular/core';
import {ApiService} from '../api.service';
import {ApiDataCacheService} from '../api-data-cache.service';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';

@Component({
  selector: 'app-log-in-form',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './log-in-form.component.html',
  styleUrl: './log-in-form.component.css'
})
export class LogInFormComponent {
  email:string = "";


  constructor(private router: Router, private apiService:ApiService, private apiCacheService: ApiDataCacheService) {

  }

  public login() {
    this.apiService.login(this.email)
      .subscribe(value => {
        this.apiService.setToken(value.token)
        this.apiCacheService.homeResponse.refresh()
        this.router.navigateByUrl("/")
      })
  }
}
