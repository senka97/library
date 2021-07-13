export interface RentalShow {
  id: number,
  book: {
    id: number,
    title: string,
  }
  renter: {
    id: number,
    username: string,
  }
  rentingDate: Date,
  returned: boolean,
  returnDate: Date,
  daysRemaining: number
}