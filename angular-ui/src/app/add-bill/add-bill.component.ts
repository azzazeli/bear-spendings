import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'app-add-bill',
  templateUrl: './add-bill.component.html',
  styleUrls: ['./add-bill.component.css']
})
export class AddBillComponent implements OnInit {

  stores: string[] = ['Nr.1', 'Ali Market', 'Pegas', 'Fourchette']
  addBillForm: FormGroup;

  constructor(private logger: NGXLogger) { }

  ngOnInit() {
    this.addBillForm = new FormGroup({
      store: new FormControl(null)
    });
  }

  onAddBill() {
    this.logger.debug("on add bill. addBillForm:", this.addBillForm );
  }

}
