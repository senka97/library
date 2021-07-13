import { NgModule } from "@angular/core";
import { JwtModule } from "@auth0/angular-jwt";

export function tokenGetter() {
  return localStorage.getItem('token');
}

@NgModule({
  imports: [JwtModule.forRoot({
    config: {
      tokenGetter: tokenGetter,
      allowedDomains: ['3.19.99.131', 'localhost:8080']
    }})
  ],
  exports: [JwtModule]
})
export class AppJwtModule { }