import { Component, ElementRef, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import { debounce } from 'lodash';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent {

  @Output() searchBooksEvent = new EventEmitter<string>();
  @ViewChild("searchInput") searchInput! : ElementRef;

  constructor() {
    this.onSearch = debounce(this.onSearch, 500)
   }

  onSearch(){
    let text = this.searchInput.nativeElement.value;
    this.searchBooksEvent.emit(text)
  }
    
}
