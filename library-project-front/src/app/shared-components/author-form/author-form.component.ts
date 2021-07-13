import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthorService } from 'src/app/service/author.service';
import { Author } from 'src/app/type/author';

@Component({
  selector: 'app-author-form',
  templateUrl: './author-form.component.html',
  styleUrls: ['./author-form.component.scss']
})
export class AuthorFormComponent implements OnInit {

  authorForm! : FormGroup;

  @Output() createAuthorEvent = new EventEmitter<Author>();

  constructor(private authorService: AuthorService,
              private router: Router) { }

  ngOnInit(): void {
    this.authorForm = new FormGroup({
      firstName: new FormControl('', Validators.required),
      middleName: new FormControl(''),
      lastName: new FormControl('', Validators.required),
    })
  }

  onAuthorSubmit(){

    let author = {
      firstName: this.authorForm.controls['firstName'].value,
      middleName: this.authorForm.controls['middleName'].value,
      lastName: this.authorForm.controls['lastName'].value
    }

    this.authorService.create(author).subscribe(
      (response) => {
        this.createAuthorEvent.emit(response);
        this.authorForm.reset();
      },
      error => {
        this.router.navigateByUrl('/error', {state: {message: error.error}});
      }
    )
  }

}
