import {Component} from '@angular/core';
import {ApiDataCacheService} from '../api-data-cache.service';
import {RouterLink, RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink
  ],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent {
  private values: { [p: string]: string } = {};

  constructor(private apiCacheService: ApiDataCacheService) {
    apiCacheService.adminQueryResp.subscribe(value => {
      this.values = value.parameters;
    })
  }
}
