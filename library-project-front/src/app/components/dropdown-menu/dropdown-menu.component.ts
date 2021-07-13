import { Component, Input } from '@angular/core';
import { DropdownItem } from 'src/app/type/dropdown-item';

@Component({
  selector: 'app-dropdown-menu',
  templateUrl: './dropdown-menu.component.html',
  styleUrls: ['./dropdown-menu.component.scss']
})
export class DropdownMenuComponent {

  @Input() label = '';
  @Input() dropdownItems: DropdownItem[] = [];

  constructor() { }

  invokeAction(action: (() => void) | undefined) {
    if (action) {
      action();
    }
  }
}
