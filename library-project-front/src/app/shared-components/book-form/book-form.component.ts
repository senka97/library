import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import { BookService } from 'src/app/service/book.service';
import { AuthorService } from 'src/app/service/author.service';
import { Author } from 'src/app/type/author';
import { environment } from 'src/environments/environment';
import { Router, ActivatedRoute } from '@angular/router';
import { NewBook } from 'src/app/type/new-book';
import { utils } from 'src/environments/environment'

@Component({
  selector: 'app-book-form',
  templateUrl: './book-form.component.html',
  styleUrls: ['./book-form.component.scss']
})
export class BookFormComponent implements OnInit {

  public bookId : string | null = null;
  public newBookForm! : FormGroup;
  public book : NewBook = {
                            title: "",
                            subtitle: "",
                            creationDate: null,
                            isbn: "",
                            quantity: 0,
                            authors: [],
                            imageUrl: ""
                          };

  public title : string = '';
  public showAlert: boolean = false;
  public alertMessage: string = '';

  public authors: Author[] = [];
  public filteredAuthors: Author[] = [];
  public selectedAuthors: Author[] = [];

  public formData = new FormData();
  public selectedFile!: File;
  public selectedFileForReader!: File;
  public imageSrc: String | null =  null;
  public imageUrl!: string; 
  public showUploadFromComputer: boolean = true;

  modalType: string = "author";
  modalTitle: string = "Create Author";

  constructor(private bookService: BookService,
              private authorService: AuthorService, 
              private router: Router,
              private route: ActivatedRoute,) { }

  ngOnInit(): void {

    this.getAuthors();

    this.bookId = this.route.snapshot.paramMap.get("id")

    if(this.bookId === null){
      
      this.title = "Create Book"
      this.initializeForm(this.book);
    }
    else{
      this.title = "Edit Book"
      this.bookService.get(this.bookId).subscribe(
        response => {
          this.initializeForm(response.body[0]);
        },
        error => {
          this.router.navigateByUrl('/error', {state: {message: error.error}});
        }
      )
    }
  }

  getAuthors(){
    this.authorService.getAll().subscribe(
      response => {
        this.authors = response;
        this.filteredAuthors = response;
      },
      error => {
        this.router.navigateByUrl('/error', {state: {message: error.error}});
      }
    )
  }

  initializeForm(book : NewBook) {
      this.selectedAuthors = book.authors;

      this.newBookForm = new FormGroup({
        title: new FormControl(book.title, Validators.required),
        description: new FormControl(book.subtitle),
        creationDate: new FormControl(book.creationDate, Validators.required),
        isbn: new FormControl(book.isbn, [Validators.pattern(utils.isbnRegex), Validators.required]),
        quantity: new FormControl(book.quantity, [Validators.required, Validators.min(0)]),
        authors: new FormControl(this.selectedAuthors),
        imageFile: new FormControl(''),
        imageWeb: new FormControl('')
      })
      this.imageSrc = book.imageUrl;
      this.imageUrl = book.imageUrl;
  }

  onSubmit() {

    if(this.newBookForm.get('imageFile')?.value != "" && this.showUploadFromComputer === true){
      this.uploadImage();
    }
    else{
      if(this.bookId == null){
        this.createBook();
      }
      else{
        this.updateBook();
      }
    }
    
  }

  uploadImage(){
    this.formData.set('image', this.selectedFile, this.selectedFile.name);
    this.bookService.uploadImage(this.formData).subscribe(
      res => {
        this.imageUrl = environment.contentUrl + "/" + res.body;
        if(this.bookId === null){
          this.createBook();
        }
        else{
          this.updateBook();
        }
      },
      error => {
        this.router.navigateByUrl('/error', {state: {message: error.error}});
      }
    );
  }

  createBook() {

    var book : NewBook = {
      title: this.newBookForm.controls['title'].value,
      subtitle: this.newBookForm.controls['description'].value,
      creationDate: this.newBookForm.controls['creationDate'].value,
      isbn: this.newBookForm.controls['isbn'].value,
      quantity: this.newBookForm.controls['quantity'].value,
      authors: this.selectedAuthors,
      imageUrl: this.imageUrl
    }

    this.bookService.create(book).subscribe(
      response => {
        this.imageSrc = null;
        this.newBookForm.reset();
        this.alertMessage = 'Book successfully created'
        this.showAlert = true;
          setTimeout(() => {
            this.showAlert = false;
            this.alertMessage = '';
          }, 5000)
      },
      error => {
        console.log(error.error);
        this.router.navigateByUrl('/error', {state: {message: error.error}});
      }
    );
  }

  updateBook() {

    var book : NewBook = {
      title: this.newBookForm.controls['title'].value,
      subtitle: this.newBookForm.controls['description'].value,
      creationDate: this.newBookForm.controls['creationDate'].value,
      isbn: this.newBookForm.controls['isbn'].value,
      quantity: this.newBookForm.controls['quantity'].value,
      authors: this.selectedAuthors,
      imageUrl: this.imageUrl
    }

    this.bookService.edit(book, this.bookId!).subscribe(
      response => {
        this.imageSrc = null;
        this.newBookForm.reset();
        this.router.navigate(['/book/',this.bookId]);
      },
      error => {
        this.router.navigateByUrl('/error', {state: {message: error.error}});
      }
    );
  }

  onSelectFile(event: any) {
    this.selectedFile = event.target.files[0] as File;
    this.selectedFileForReader = event.target.files[0] as File;

    const reader = new FileReader();
    reader.readAsDataURL(this.selectedFileForReader);
    reader.onload = () => {
      this.imageSrc = reader.result as string;
    };
  }

  onChosenUrl(event: any) {
    this.imageSrc = event.target.value;
    this.imageUrl = event.target.value;
  }

  radioButtonChanged(event: any){
    let radioValue = event.target['value'];
    if(radioValue == 1){
      this.showUploadFromComputer = true;
    }else{
      this.showUploadFromComputer = false;
    }
  }

  filterAuthors(event: any){
    this.filteredAuthors = this.authors;
    var inputValue = event.target.value.toLowerCase();
    this.filteredAuthors = this.filteredAuthors.filter(a => (this.getFullName(a)).includes(inputValue));
  }

  getFullName(author: Author) : string {
      return author.firstName.toLowerCase() + " " + author.lastName.toLowerCase()
  }

  addAuthor(newAuthor: Author) {
    this.filteredAuthors.push(newAuthor);
    this.selectAuthor(newAuthor);
  }

  selectAuthor(author: Author){
    if(this.isSelected(author)){
      var index = this.selectedAuthors.indexOf(author);
      this.selectedAuthors.splice(index, 1);
    }
    else{
      this.selectedAuthors.push(author);
    }
  }

  isSelected(author : Author){
    var found = false;
    if (this.selectedAuthors.some(a => a.id === author.id)) {
        found = true
    }
    return found;
  }

}
