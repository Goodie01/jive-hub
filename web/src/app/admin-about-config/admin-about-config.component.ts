import {Component} from '@angular/core';
import {ApiDataCacheService} from '../api-data-cache.service';
import {KeyValuePipe, NgForOf} from '@angular/common';

@Component({
  selector: 'app-admin-about-config',
  standalone: true,
  imports: [
    KeyValuePipe,
    NgForOf
  ],
  templateUrl: './admin-about-config.component.html',
  styleUrl: './admin-about-config.component.css'
})
export class AdminAboutConfigComponent {
  values: { [p: string]: string } = {};

  constructor(private api: ApiDataCacheService) {
    api.adminQueryResp.subscribe(value => {
      this.values = value.parameters;
    })
  }
}
