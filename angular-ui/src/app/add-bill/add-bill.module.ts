import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AddBillComponent} from './add-bill.component';
import {NewBillItemComponent} from './new-bill-item/new-bill-item.component';
import {ReactiveFormsModule} from '@angular/forms';
import {CalendarModule} from 'primeng/primeng';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {CoreModule} from '../core/core.module';
import {ToastModule} from 'primeng/toast';
import {TooltipModule} from 'primeng/tooltip';
import {AutoCompleteModule} from 'primeng/autocomplete';

@NgModule({
  declarations: [
    AddBillComponent,
    NewBillItemComponent
  ],
  imports: [
    CommonModule,
    CoreModule,
    ReactiveFormsModule,
    CalendarModule,
    BrowserAnimationsModule,
    ToastModule,
    TooltipModule,
    AutoCompleteModule
  ],
  exports: [
    AddBillComponent
  ]
})
export class AddBillModule { }
