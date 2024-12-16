import {Component} from '@angular/core';
import {ApiDataCacheService} from '../api-data-cache.service';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-admin-about-config',
  standalone: true,
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './admin-about-config.component.html',
  styleUrl: './admin-about-config.component.css'
})
export class AdminAboutConfigComponent {
  values: ModifiableConfigurationValue[] = [];


  constructor(private api: ApiDataCacheService) {
    api.adminQueryResp.subscribe(value => {
      this.values = value.parameters
        .map(value1 => {
          return new ModifiableConfigurationValue(value1.name, value1.value, value1.writeable)
        })
    })
  }

  showSaveInput():boolean {
    return this.values.map(value => value.modified)
      .some(value => value);
  }
}

class ModifiableConfigurationValue {
  name: string;
  value: string;
  writeable: boolean;
  modified: boolean;


  constructor(name: string, value: string, writeable: boolean) {
    this.name = name;
    this.value = value;
    this.writeable = writeable;
    this.modified = false;
  }
}
