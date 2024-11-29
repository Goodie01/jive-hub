import {Routes} from '@angular/router';
import {CustWebComponent} from './cust-web/cust-web.component';
import {AdminComponent} from './admin/admin.component';
import {LogInFormComponent} from './log-in-form/log-in-form.component';
import {AdminDashboardComponent} from './admin-dashboard/admin-dashboard.component';
import {AdminAboutConfigComponent} from './admin-about-config/admin-about-config.component';

export const routes: Routes = [
  {path: 'login', component: LogInFormComponent},
  {path: 'admin', component: AdminComponent, children: [
      {path: '', component: AdminDashboardComponent},
      {path: 'about:config', component: AdminAboutConfigComponent},
    ]},
  {path: '**', component: CustWebComponent},
];
