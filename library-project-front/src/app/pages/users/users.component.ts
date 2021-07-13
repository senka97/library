import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { UserService } from 'src/app/service/user.service';
import { NewUser } from 'src/app/type/new-user';
import { User } from 'src/app/type/user';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {

  users : NewUser[] = []

  modalType: string = "user";
  modalTitle: string = "";
  modalAction!: "create" | "edit";
  user: NewUser = {
    id: null,
    username: '',
    firstName: '',
    lastName: '',
    password: '',
    role: '',
  };
  loggedInUser!: User;
  userForm! : FormGroup

  constructor(private router: Router,
              private userService: UserService,
              private jwtService: JwtHelperService) { }

  ngOnInit(): void {
    this.getLoggedInUser();
    this.getUsers();
  }

  getUsers(){
    this.userService.getAll().subscribe(
      response => {
        this.users = response.body;
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

  createNewUser(){
    this.modalTitle = "Create User"
    this.modalAction = 'create'
    this.userForm = new FormGroup({
      username: new FormControl('', Validators.required),
      firstName: new FormControl('', Validators.required),
      lastName: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
    })
  }

  editUser(user : NewUser){
    event?.stopPropagation();
    this.modalTitle = "Edit User"
    this.modalAction = 'edit'
    this.user = user;
    this.userForm = new FormGroup({
      username: new FormControl(this.user.username, Validators.required),
      firstName: new FormControl(this.user.firstName, Validators.required),
      lastName: new FormControl(this.user.lastName, Validators.required),
    })
  }

  deleteUser(user: NewUser){
    event?.stopPropagation();
    if(confirm("Are you sure you want to delete: " + user.firstName + " " + user.lastName)){
      var index = this.users.indexOf(user);
      this.userService.delete(user.username).subscribe(
        response => {
          this.users.splice(index, 1);
        },
        error => {
          this.router.navigateByUrl('/error', {state: {message: error.error}});
        }
      )
    }
  }

  goToUserDetails(username : string){
    this.router.navigate(['/profile/', username])
  }

  addUserEvent(user: NewUser){
    this.users.push(user);
  }

  editUserEvent(){
    this.getUsers(); 
  }
  
  canDelete(user : NewUser) : boolean {
    return this.loggedInUser.username !== user.username
  }

}
