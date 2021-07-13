import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { AuthService } from '../service/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  
  constructor(public auth: AuthService, 
              public router: Router,
              public jwtService: JwtHelperService ) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {

    const token = localStorage.getItem('token');
    
    if (token) {
      const tokenPayload = this.jwtService.decodeToken(token);

      if (tokenPayload.role == "ROLE_ADMIN") {
        return true;
      }
    }
    this.router.navigate(['login']);
    return false;

  }
}
