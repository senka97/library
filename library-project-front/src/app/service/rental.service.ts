import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { AuthorCreate } from '../type/author-create';
import { RentalShow } from '../type/rental-show';
import { RentalCreate } from '../type/rental-create';

@Injectable({
  providedIn: 'root'
})
export class RentalService {

  rent_route: String = "/rent";
  user_rentals_route: String = "/user"
  logged_in_user_rentals_route: String = "/personal"

  constructor(private http: HttpClient) { 
  }

  getAll(): Observable<any> {
    return this.http.get(environment.apiUrl + this.rent_route, {observe: 'response'})
  }

  getAllForUser(username: string): Observable<any> {
    return this.http.get(environment.apiUrl + this.rent_route + this.user_rentals_route + '/' + username, {observe: 'response'})
  }

  getAllForLoggedInUser(): Observable<any> {
    return this.http.get(environment.apiUrl + this.rent_route + this.logged_in_user_rentals_route , {observe: 'response'})
  }

  create(request: RentalCreate) : Observable<any>{
    return this.http.post(environment.apiUrl + this.rent_route, request) 
  }

  returnBook(id: number){
    return this.http.put(environment.apiUrl + this.rent_route + "/" + id, {observe: 'response'})
  }
}