// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  baseUrl: 'http://3.19.99.131',
  apiUrl: 'http://3.19.99.131/api',
  contentUrl: 'http://3.19.99.131/content'
};

export const imageConstants = {
  noCoverImage: 'https://cdn.picpng.com/book/book-view-30965.png'
}

export const utils = {
  isbnRegex : /(?=(?:\D*\d){10}(?:(?:\D*\d){3})?$)[\d-]+$/
}
/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
