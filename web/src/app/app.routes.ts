import {Routes} from '@angular/router';
import {CustWebComponent} from './cust-web/cust-web.component';
import {AdminComponent} from './admin/admin.component';
import {LogInFormComponent} from './log-in-form/log-in-form.component';

export const routes: Routes = [
  {path: 'login', component: LogInFormComponent},
  {path: 'admin', component: AdminComponent},
  {path: '**', component: CustWebComponent},
];
