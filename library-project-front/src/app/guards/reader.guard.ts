import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router} from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { AuthService } from '../service/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ReaderGuard implements CanActivate {
  constructor(public auth: AuthService, 
    public router: Router,
    public jwtService: JwtHelperService ) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {

    const token = localStorage.getItem('token');
     this.jwtService.isTokenExpired(token || undefined);

     if (!this.isAuthenticated()) {
      this.router.navigate(['login']);
      return false;
    }
    return true;

  }

  public isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    return !this.jwtService.isTokenExpired(token || undefined);
  }
  
  
}
