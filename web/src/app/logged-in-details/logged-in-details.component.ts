import {Component} from '@angular/core';
import {ApiDataCacheService} from '../api-data-cache.service';
import {User} from '../rest';
import {NgIf} from '@angular/common';
import {Router, RouterLink} from '@angular/router';
import {ApiService} from '../api.service';

@Component({
  selector: 'app-logged-in-details',
  standalone: true,
  imports: [
    NgIf,
    RouterLink
  ],
  templateUrl: './logged-in-details.component.html',
  styleUrl: './logged-in-details.component.css'
})
export class LoggedInDetailsComponent {
  user?:User = undefined;

  constructor(private apiCache:ApiDataCacheService, private apiService: ApiService, private router: Router) {
    apiCache.homeResp(value => {
      this.user = value.loggedInUser
    })
  }

  logout() {
    this.apiService.unsetToken()
    this.apiCache.refreshHomeResp()
    this.router.navigateByUrl("/")
  }
}
