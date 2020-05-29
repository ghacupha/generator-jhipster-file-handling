import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IFileUpload } from 'app/shared/model/fileUploads/file-upload.model';

@Component({
  selector: 'gha-file-upload-detail',
  templateUrl: './file-upload-detail.component.html',
})
export class FileUploadDetailComponent implements OnInit {
  fileUpload: IFileUpload | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileUpload }) => (this.fileUpload = fileUpload));
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
