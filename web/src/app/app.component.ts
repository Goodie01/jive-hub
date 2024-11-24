import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {ApiService} from "./api.service";
import {Title} from "@angular/platform-browser";
import {NgForOf} from '@angular/common';
import {MenuItem} from './rest';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NgForOf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  menu: MenuItem[] = []

  constructor(private apiService: ApiService, private titleService:Title) {
    apiService.homeResponse().subscribe(value => {
      this.menu = value.menuItems
    })
  }
}
