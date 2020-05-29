import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IFileType } from 'app/shared/model/fileUploads/file-type.model';

@Component({
  selector: 'gha-file-type-detail',
  templateUrl: './file-type-detail.component.html',
})
export class FileTypeDetailComponent implements OnInit {
  fileType: IFileType | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileType }) => (this.fileType = fileType));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
