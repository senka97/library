import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { RentalService } from 'src/app/service/rental.service';
import { UserService } from 'src/app/service/user.service';
import { NewUser } from 'src/app/type/new-user';
import { RentalShow } from 'src/app/type/rental-show';
import { User } from 'src/app/type/user';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  username!: string | null; //querry param
  user: NewUser ={
    firstName: '',
    lastName: '',
    username: '',
    password: '',
    id: null,
    role: ''
  };
  loggedInUser!: User;
  rentals: RentalShow[] = [];

  modalType = "user"
  modalTitle = "Edit User"
  userForm! : FormGroup;
  title = "Edit User"

  constructor(private route: ActivatedRoute,
              private router: Router,
              private userService: UserService,
              private jwtService: JwtHelperService,
              private rentalService: RentalService) { }

  ngOnInit(): void {
    this.getLoggedInUser();
    this.username = this.route.snapshot.paramMap.get("username")
    if(this.username === null){
      this.getUserProfile()
    }
    else{
      this.getUser(this.username);
    }
  }

  getUser(username: string){
    this.userService.get(username).subscribe(
      response => {
        this.user = response.body;
        this.getRentalsForUser();
      },
      error => {
        this.router.navigateByUrl('/error', {state: {message: error.error}});
      }
    )
  }

  getUserProfile(){
    this.userService.getUserProfile().subscribe(
      response => {
        this.user = response.body;
        this.getRentalsForLoggedInUser();
      },
      error => {
        this.router.navigateByUrl('/error', {state: {message: error.error}});
      }
    )
  }

  getLoggedInUser(){
    const token = localStorage.getItem('token');
    if (token) {
      this.loggedInUser = this.jwtService.decodeToken(token);
    }
  }

  getRentalsForUser(){
    this.rentalService.getAllForUser(this.user.username).subscribe(
      response => {
        let rentalsArray : RentalShow[] = response.body;
        this.rentals = this.sortArray(rentalsArray);
      },
      error => {
        this.router.navigateByUrl('/error', {state: {message: error.error}});
      }
    )
  }

  sortArray(rentals : RentalShow[]) : RentalShow[]{
    return rentals.sort((a, b) => (a.daysRemaining < b.daysRemaining ? -1 : 1));
  }

  getRentalsForLoggedInUser(){
    this.rentalService.getAllForLoggedInUser().subscribe(
      response => {
        let rentalsArray : RentalShow[] = response.body;
        this.rentals = this.sortArray(rentalsArray);
      },
      error => {
        this.router.navigateByUrl('/error', {state: {message: error.error}});
      }
    )
  }

  deleteUser(){
    if(confirm("Are you sure you want to delete: " + this.user.firstName + " " + this.user.lastName)){
      this.userService.delete(this.user.username).subscribe(
        response => {
          this.router.navigate(['/users'])
        },
        error => {
          this.router.navigateByUrl('/error', {state: {message: error.error}});
        }
      )
    }
  }

  editUser(){
    this.userForm = new FormGroup({
      username: new FormControl(this.user.username, Validators.required),
      firstName: new FormControl(this.user.firstName, Validators.required),
      lastName: new FormControl(this.user.lastName, Validators.required),
    })
  }

  editUserEvent(){
    this.getUser(this.username!);
  }
  
  returnBook(id: number){
    event?.stopPropagation();
    this.rentalService.returnBook(id).subscribe(
      response => {
        this.getRentalsForUser();
      },
      error => {
        this.router.navigateByUrl('/error', {state: {message: error.error}});
      }
    )
  }

  goToBookDetails(rental : RentalShow){
    this.router.navigate(['/book/',rental.book.id]);
  }

}
