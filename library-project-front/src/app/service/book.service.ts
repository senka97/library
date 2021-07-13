import { Injectable } from '@angular/core';
import { NewBook } from '../type/new-book';
import {HttpClient} from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  books_route: String = "/books";
  images_route: String = "/images"

  constructor(private http: HttpClient) { 
  }

  create(request: NewBook){
    return this.http.post(environment.apiUrl + this.books_route, request) 
  }

  uploadImage(formData: FormData) : Observable<any>{
    return this.http.post(environment.apiUrl + this.images_route, formData, {observe: 'response', responseType: 'text'})
  }

  getAll() : Observable<any>{
    return this.http.get(environment.apiUrl + this.books_route, { observe: 'response' })
  }

  getBooksPage(page: number, searchText : string) : Observable<any>{
    let queryParams = '';
    if(searchText != '')
    {
      queryParams = '&title=' + searchText + '&subtitle=' + searchText + '&firstName=' + searchText + '&middleName=' + searchText + '&lastName=' + searchText;
    }
    return this.http.get(environment.apiUrl + this.books_route + "?page=" + page + queryParams, { observe: 'response' })
  }

  get(id: string) : Observable<any>{
    return this.http.get(environment.apiUrl + this.books_route + "?id=" + id, { observe: 'response' })
  }

  edit(request: NewBook, id: string) : Observable<any> {
    return this.http.put(environment.apiUrl + this.books_route + "/" + id , request, {observe: 'response'})
  }

  delete(id: number){
    return this.http.delete(environment.apiUrl + this.books_route + "/" + id , {observe: 'response'})
  }
}