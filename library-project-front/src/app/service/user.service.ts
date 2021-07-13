import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { NewUser } from '../type/new-user';
import { EditUser } from '../type/edit-user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  users_route: String = "/users";
  user_profile: String = "/profile"

  constructor(private http: HttpClient) { 
  }

  create(request: NewUser) : Observable<any>{
    return this.http.post(environment.apiUrl + this.users_route, request) 
  }

  getAll() : Observable<any>{
    return this.http.get(environment.apiUrl + this.users_route, { observe: 'response' })
  }

  get(username: string) : Observable<any>{
    return this.http.get(environment.apiUrl + this.users_route + "/" + username, { observe: 'response' })
  }

  getUserProfile() : Observable<any>{
    return this.http.get(environment.apiUrl + this.users_route + this.user_profile, { observe: 'response' })
  }

  edit(request: EditUser, id: number) : Observable<any> {
    return this.http.put(environment.apiUrl + this.users_route + "/" + id , request, {observe: 'response'})
  }

  delete(username: string){
    return this.http.delete(environment.apiUrl + this.users_route + "/" + username , {observe: 'response'})
  }
}