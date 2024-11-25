import {Component} from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {Title} from "@angular/platform-browser";
import {NgForOf} from '@angular/common';
import {MenuItem} from './rest';
import {ApiDataCacheService} from './api-data-cache.service';
import {LoggedInDetailsComponent} from './logged-in-details/logged-in-details.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgForOf, LoggedInDetailsComponent, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  menu: MenuItem[] = []

  constructor(private apiService: ApiDataCacheService, private titleService:Title) {
    apiService.homeResp(value => {
      this.menu = value.menuItems
    })
  }
}
