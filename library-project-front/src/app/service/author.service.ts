import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { Author } from '../type/author';
import { AuthorCreate } from '../type/author-create';

@Injectable({
  providedIn: 'root'
})
export class AuthorService {

  authors_route: String = "/authors";

  constructor(private http: HttpClient) { 
  }

  getAll(): Observable<Author[]> {
    return this.http.get<Author[]>(environment.apiUrl + this.authors_route)
  }

  create(request: AuthorCreate) : Observable<any>{
    return this.http.post(environment.apiUrl + this.authors_route, request) 
  }
}