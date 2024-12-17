import {Component} from '@angular/core';
import {ApiDataCacheService} from '../api-data-cache.service';
import {NgForOf, NgIf} from '@angular/common';
import {ApiService} from '../api.service';
import {UpdateValue} from '../rest';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-admin-about-config',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    FormsModule
  ],
  templateUrl: './admin-about-config.component.html',
  styleUrl: './admin-about-config.component.css'
})
export class AdminAboutConfigComponent {
  values: ModifiableConfigurationValue[] = [];


  constructor(private api: ApiDataCacheService, private apiService: ApiService) {
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

  saveChanges() {
    var values: UpdateValue[] = this.values.filter(value => value.modified)
      .map(value => {
        return {
          name: value.name,
          value: value.value
        }
      })

    this.apiService.updateAdmin(values)
      .subscribe(value => {
        this.api.adminQueryResp.refresh(); //TODO find a way to set this from outside
        this.api.homeResponse.refresh();
    })
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
