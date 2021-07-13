import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { BookService } from 'src/app/service/book.service';
import { RentalService } from 'src/app/service/rental.service';
import { Book } from 'src/app/type/book';
import { RentalCreate } from 'src/app/type/rental-create';
import { User } from 'src/app/type/user';
import { imageConstants} from 'src/environments/environment';

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.scss']
})
export class BookDetailsComponent implements OnInit  {

  public bookId : string | null = "";
  public book : Book = {
    id: 0,
    title: "",
    subtitle: "",
    creationDate: null,
    isbn: "",
    quantity: 0,
    imageUrl: '',
    authors: []
  };

  constructor(private bookService: BookService,
              private router: Router,
              private route: ActivatedRoute,
              private jwtService: JwtHelperService,
              private rentalService: RentalService) { }

  ngOnInit(): void {

    this.bookId = this.route.snapshot.paramMap.get("id")

    this.bookService.get(this.bookId!).subscribe(
      response => {
        this.book = response.body[0];
        if(this.book.imageUrl === null || this.book.imageUrl === ''){
          this.book.imageUrl = imageConstants.noCoverImage;
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

  deleteBook(event : any){
    if(confirm("Are you sure you want to delete: " + this.book.title)){
      this.bookService.delete(this.book.id).subscribe(
        response => {
          this.router.navigateByUrl('/');
        },
        error => {
          this.router.navigateByUrl('/error', {state: {message: error.error}});
        }
      )
    }else{
      event.stopPropagation();
    }
  }

  rentBook(){
    if(confirm("Are you sure you want to rent book: " + this.book.title)){
      var rental : RentalCreate = {
        bookId: this.book.id
      }
      this.rentalService.create(rental).subscribe(
        response => {
          this.router.navigate(['/profile']);
        },
        error => {
          this.router.navigateByUrl('/error', {state: {message: error.error}});
        }
      )
    }
  }

}
