import { Component, EventEmitter, Input, Output, OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { BookService } from 'src/app/service/book.service';
import { RentalService } from 'src/app/service/rental.service';
import { Book } from 'src/app/type/book';
import { RentalCreate } from 'src/app/type/rental-create';
import { User } from 'src/app/type/user';
import { imageConstants} from 'src/environments/environment';

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrls: ['./book-card.component.scss']
})
export class BookCardComponent implements OnInit {

  @Input() book! : Book;

  @Output() deleteBookEvent = new EventEmitter<Book>();

  constructor(private jwtService : JwtHelperService,
              private router: Router,
              private bookService: BookService,
              private rentalService: RentalService) { }

  get user(): User | null {
    const token = localStorage.getItem('token');
    if (token) {
      return this.jwtService.decodeToken(token);
    }
    return null;
  }

  ngOnInit() : void{
    if(this.book.imageUrl === '' || this.book.imageUrl === null){
      this.book.imageUrl = imageConstants.noCoverImage;
    }
  }
  
  goToBookDetails(){
    this.router.navigate(['/book/', this.book.id]);
  }

  deleteBook(event : any){
    event.stopPropagation();
    if(confirm("Are you sure you want to delete: " + this.book.title)){
      this.bookService.delete(this.book.id).subscribe(
        response => {
          this.deleteBookEvent.emit(this.book!);
        },
        error => {
          this.router.navigateByUrl('/error', {state: {message: error.error}});
        }
      )
    }
  }

  rentBook(){
    event?.stopPropagation();
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

  isAdmin(){
    return this.user?.role === 'ROLE_ADMIN'
  }

}
