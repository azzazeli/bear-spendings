import {enableProdMode} from '@angular/core';
import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';

import {AppModule} from './app/app.module';
import {environment} from './environments/environment';

if (environment.production) {
  enableProdMode();
  environment.API_URL = window.location.origin + '/api/v1/';
  console.log(`apiUrl:${environment.API_URL}`);
}

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
