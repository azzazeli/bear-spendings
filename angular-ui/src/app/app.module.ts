import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { HeaderComponent } from './header/header.component';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { HttpClientModule } from '@angular/common/http';
import { StoreService } from './service/store.service';
import { AddBillModule } from './add-bill/add-bill.module';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    HeaderComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    AddBillModule,
    LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG} )
  ],
  providers: [StoreService],
  bootstrap: [AppComponent]
})
export class AppModule { }
