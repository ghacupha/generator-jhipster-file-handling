import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFileUpload } from 'app/shared/model/fileUploads/file-upload.model';
import { FileUploadService } from './file-upload.service';

@Component({
  templateUrl: './file-upload-delete-dialog.component.html',
})
export class FileUploadDeleteDialogComponent {
  fileUpload?: IFileUpload;

  constructor(
    protected fileUploadService: FileUploadService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fileUploadService.delete(id).subscribe(() => {
      this.eventManager.broadcast('fileUploadListModification');
      this.activeModal.close();
    });
  }
}
