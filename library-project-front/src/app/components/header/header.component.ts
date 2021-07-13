import { Component } from '@angular/core';
import { DropdownItem } from 'src/app/type/dropdown-item';
import { User } from 'src/app/type/user';
import { JwtHelperService } from '@auth0/angular-jwt'
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  constructor(private jwtService: JwtHelperService,
              private router: Router) {}

  get user(): User | null {
    const token = localStorage.getItem('token');
    if (token) {
      return this.jwtService.decodeToken(token);
    }
    return null;
  }

  get username(): string {
    if (this.user?.username) {
      return this.user?.username;
    }
    return '';
  }

  logout = () => {
    localStorage.removeItem('token');
    this.router.navigate(['/']);
  }

  get dropdownItems(): DropdownItem[] {
    let items: DropdownItem[] = []
    if (this.user?.role === 'ROLE_ADMIN') {
      items = [
        { text: 'Profile', link: '/profile' },
        { text: 'Rentals', link: '/rentals' },
        { text: 'Users', link: '/users' }
      ]
    }
    else if (this.user?.role === 'ROLE_USER') {
      items = [{
         text: 'Profile', link: '/profile' },]
        
    }
    items.push({ text: 'Log out', action: this.logout })

    return items;
  }

}
