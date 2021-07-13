import { Injectable } from '@angular/core';
import { AuthRequest } from '../type/auth-request';
import {HttpClient} from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  login_route: String = "/auth";

  constructor(private http: HttpClient) { 
  }

  login(request: AuthRequest) {
    return this.http.post(environment.apiUrl + this.login_route, request, {observe: 'response'})
  }
}
