import {Routes} from '@angular/router';
import {CustWebComponent} from './cust-web/cust-web.component';
import {AdminComponent} from './admin/admin.component';

export const routes: Routes = [
  {path: 'admin', component: AdminComponent},
  {path: '**', component: CustWebComponent},
];
