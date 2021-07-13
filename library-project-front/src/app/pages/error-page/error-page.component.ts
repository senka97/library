import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-error-page',
  templateUrl: './error-page.component.html',
  styleUrls: ['./error-page.component.scss']
})
export class ErrorPageComponent implements OnInit {

  public message: string = 'Something went wrong :(';

  ngOnInit(): void {
    if(history.state.message){
      this.message = history.state.message;
    }
  }

}
