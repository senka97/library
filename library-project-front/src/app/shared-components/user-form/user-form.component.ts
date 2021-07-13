import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { UserService } from 'src/app/service/user.service';
import { NewUser } from 'src/app/type/new-user';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent {

  @Input() userForm! : FormGroup;
  @Input() actionType!: 'create' | 'edit';

  @Input() user : NewUser = {
    id: null,
    username: '',
    firstName: '',
    lastName: '',
    password: '',
    role: ''
  };

  @Output() createUserEvent = new EventEmitter<NewUser>();
  @Output() editUserEvent = new EventEmitter();

  showError: boolean = false;
  userError: string = '';

  constructor(private userService: UserService) { }

  onUserSubmit(){
    if(this.actionType === 'create'){
      let user = {
        id: null,
        role: '',
        username: this.userForm.controls['username'].value,
        firstName: this.userForm.controls['firstName'].value,
        lastName: this.userForm.controls['lastName'].value,
        password: this.userForm.controls['password'].value
      }
  
      this.userService.create(user).subscribe(
        (response) => {
          this.createUserEvent.emit(response);
          this.userForm.reset();
        },
        error => {
          this.userError = error.error;
          this.showError = true;
          setTimeout(() => {
            this.showError = false;
          }, 5000)
        })
    }
    else{
      let user = {
        username: this.userForm.controls['username'].value,
        firstName: this.userForm.controls['firstName'].value,
        lastName: this.userForm.controls['lastName'].value,
      }
  
      this.userService.edit(user, this.user.id!).subscribe(
        (response) => {
          this.editUserEvent.emit();
          this.userForm.reset();
        },
        error => {
          this.userError = error.error;
          this.showError = true;
          setTimeout(() => {
            this.showError = false;
          }, 5000)
        })
    }
  }
}
