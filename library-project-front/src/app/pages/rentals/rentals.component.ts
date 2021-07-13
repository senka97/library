import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RentalService } from 'src/app/service/rental.service';
import { RentalShow } from 'src/app/type/rental-show';

@Component({
  selector: 'app-rentals',
  templateUrl: './rentals.component.html',
  styleUrls: ['./rentals.component.scss']
})
export class RentalsComponent implements OnInit {

  rentals: RentalShow[] = []

  constructor(private router: Router,
              private rentalService: RentalService) { }

  ngOnInit(): void {
    this.getRentals();
  }

  getRentals(){
    this.rentalService.getAll().subscribe(
      response => {
        let rentalsArray : RentalShow[] = response.body;
        this.rentals = this.sortArray(rentalsArray);
      },
      error => {
        this.router.navigateByUrl('/error', {state: {message: error.error}});
      }
    )
  }

  sortArray(rentals : RentalShow[]) : RentalShow[]{
    return rentals.sort((a, b) => (a.daysRemaining < b.daysRemaining ? -1 : 1));
  }

  goToUserProfile(username: string){
    this.router.navigate(['/profile/', username]);
  }

}
