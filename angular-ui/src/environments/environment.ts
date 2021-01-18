// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  mock: false,
  dev: false,

  dateFormat: 'YYYY-MM-DD',

  API_URL: 'api/',
  BILLS_URL: 'bills',
  EXPORT_ALL_BILLS_URL: 'export_all',

  STORES_URL: 'stores',
  TOP_PRODUCTS_URL: 'top_products',

  PRODUCTS_URL: 'products',
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
