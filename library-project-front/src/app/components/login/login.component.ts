import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/service/auth.service';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  _showError: boolean = false;

  constructor(private _authService: AuthService,
              private _router: Router) {}

  loginForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
  })

  onSubmit() {
    this._authService.login({
      username: this.loginForm.controls['username'].value,
      password: this.loginForm.controls['password'].value
    }).subscribe(
      response => {
        const token = response.headers.get('Authorization');
        if (token) {
          localStorage.setItem('token', token);
          this._router.navigate(['/']);
        }
      },
      error => {
        this._router.navigateByUrl('/error', {state: {message: error.error}});
      })
  }

}
