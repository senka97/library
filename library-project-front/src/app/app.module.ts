import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Routes, RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { AppJwtModule } from './app-jwt.module';

import { AppComponent } from './app.component';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { HeaderComponent } from './components/header/header.component';
import { SearchComponent } from './shared-components/search/search.component';
import { BookCardComponent } from './shared-components/book-card/book-card.component';
import { BookDetailsComponent } from './pages/book-details/book-details.component';
import { LoginComponent } from './components/login/login.component';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { UsersComponent } from './pages/users/users.component';
import { BookFormComponent } from './shared-components/book-form/book-form.component';
import { RentalsComponent } from './pages/rentals/rentals.component';
import { ErrorPageComponent } from './pages/error-page/error-page.component';
import { DropdownMenuComponent } from './components/dropdown-menu/dropdown-menu.component';
import { AdminGuard } from './guards/admin.guard';
import { ReaderGuard } from './guards/reader.guard';
import { ModalDialogComponent } from './shared-components/modal-dialog/modal-dialog.component';
import { UserFormComponent } from './shared-components/user-form/user-form.component';
import { AuthorFormComponent } from './shared-components/author-form/author-form.component';

const routes: Routes = [
  { path: '', component: LandingPageComponent },
  { path: 'login', component: LoginComponent },
  { path: 'book/edit/:id', component: BookFormComponent,  canActivate: [AdminGuard]},
  { path: 'book/new', component: BookFormComponent, canActivate: [AdminGuard]},
  { path: 'book/:id', component: BookDetailsComponent },
  { path: 'profile/:username', component: UserProfileComponent, canActivate: [AdminGuard] },
  { path: 'profile', component: UserProfileComponent, canActivate: [ ReaderGuard] },
  { path: 'users', component: UsersComponent, canActivate: [AdminGuard] },
  { path: 'rentals', component: RentalsComponent, canActivate: [AdminGuard] },
  { path: '**', component:  ErrorPageComponent},
];

@NgModule({
  declarations: [
    AppComponent,
    LandingPageComponent,
    HeaderComponent,
    SearchComponent,
    BookCardComponent,
    BookDetailsComponent,
    LoginComponent,
    UserProfileComponent,
    DropdownMenuComponent,
    BookFormComponent,
    ModalDialogComponent,
    ErrorPageComponent,
    BookCardComponent,
    SearchComponent,
    UsersComponent,
    RentalsComponent,
    UserFormComponent,
    AuthorFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    AppJwtModule,
    RouterModule.forRoot(routes),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
