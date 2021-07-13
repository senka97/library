import { Component, OnInit, Input, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import { Author } from 'src/app/type/author';
import { NewUser } from 'src/app/type/new-user';

@Component({
  selector: 'app-modal-dialog',
  templateUrl: './modal-dialog.component.html',
  styleUrls: ['./modal-dialog.component.scss']
})
export class ModalDialogComponent implements OnInit {

  @Input() modalType = '';
  @Input() title = '';
  @Input() action!: 'create' | 'edit';
  @Input() user : NewUser = {
    id: null,
    username: '',
    firstName: '',
    lastName: '',
    password: '',
    role: ''
  };
  @Input() userForm! : FormGroup;

  @Output() createAuthorEvent = new EventEmitter<Author>();
  @Output() createUserEvent = new EventEmitter<NewUser>();
  @Output() editUserEvent = new EventEmitter();
  
  @ViewChild('closeModal') closeModal!: ElementRef;

  public authorForm! : FormGroup; 
  showError : boolean = false;
  userError: string = '';

  constructor() { }

  ngOnInit(): void {
    this.authorForm = new FormGroup({
      firstName: new FormControl('', Validators.required),
      middleName: new FormControl(''),
      lastName: new FormControl('', Validators.required),
    })
  }

  addAuthor(author : Author){
    this.createAuthorEvent.emit(author);
  }

  addUserEvent(user : NewUser){
    this.createUserEvent.emit(user);
    this.closeModal.nativeElement.click();
  }

  updateUserEvent(){
    this.editUserEvent.emit();
    this.closeModal.nativeElement.click();
  }
  
}
