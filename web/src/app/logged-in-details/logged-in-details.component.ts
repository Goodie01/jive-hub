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
    apiCache.homeResponse.subscribe(value => {
      this.user = value.loggedInUser
    })
  }

  logout() {
    this.apiService.unsetToken()
    this.apiCache.homeResponse.refresh()
    this.router.navigateByUrl("/")
  }
}
