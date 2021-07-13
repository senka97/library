import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/type/user';
import { JwtHelperService } from '@auth0/angular-jwt'
import { Book } from 'src/app/type/book';
import { BookService } from 'src/app/service/book.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss']
})
export class LandingPageComponent implements OnInit {

  books : Book[] = [];
  page : number = 0;
  noMoreBooks : boolean = false;
  searchText : string = '';
  showAlert: boolean = false;
  alertMessage: string = '';

  constructor(private jwtService: JwtHelperService,
              private bookService: BookService,
              private router: Router) {
    
  }

  ngOnInit(): void {
    this.getBooks(this.page, this.searchText);
  }

  getBooks(page : number, searchText : string){
    this.bookService.getBooksPage(page, searchText).subscribe(
      response => {
        let booksResult = response.body;
        if(booksResult.length > 0){
          this.books = this.books.concat(booksResult);
        }
        else{
          this.noMoreBooks = true;
        }
      },
      error => {
        this.router.navigateByUrl('/error', {state: {message: error.error}});
      }
    )
  }

  get user(): User | null {
    const token = localStorage.getItem('token');
    if (token) {
      return this.jwtService.decodeToken(token);
    }
    return null;
  }

  onScroll(){
    if(!this.noMoreBooks){
      if(window.scrollY + window.innerHeight >= document.documentElement.scrollHeight){
        this.page++;
        this.getBooks(this.page, this.searchText);
      }
    }
  }

  searchBooks(text : string) {
    this.page = 0;
    this.books = [];
    this.noMoreBooks = false;
    this.searchText = text
    this.getBooks(this.page, this.searchText)
  }

  deleteBook(book : Book){
    if(this.books.some(b => b.id === book.id)){
      var index = this.books.indexOf(book);
      this.books.splice(index, 1);
      this.alertMessage = 'Book successfully deleted'
        this.showAlert= true;
          setTimeout(() => {
            this.showAlert = false;
            this.alertMessage = '';
          }, 5000)
    }
  }

}
