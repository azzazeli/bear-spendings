import {Component, OnInit} from '@angular/core';
import {NGXLogger} from 'ngx-logger';
import {BillService} from '../core/service/bill.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private logger: NGXLogger, private billService: BillService) {
  }

  ngOnInit() {
  }

  exportAll() {
    this.billService.exportAll().subscribe(response => {
        this.logger.log('success', response);
        const blob: Blob = new Blob([response], { type: 'application/vnd.ms-excel.sheet.macroEnabled.12' });
        const fileName = 'export-all.xlsm';
        const objectUrl: string = URL.createObjectURL(blob);
        const a: HTMLAnchorElement = document.createElement('a') as HTMLAnchorElement;

        a.href = objectUrl;
        a.download = fileName;
        document.body.appendChild(a);
        a.click();

        document.body.removeChild(a);
        URL.revokeObjectURL(objectUrl);

      }, (error) => this.logger.log(`Error downloading the file ${error}`),
      () => this.logger.info('File downloaded successfully'));
  }
}
