import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFileType } from 'app/shared/model/fileUploads/file-type.model';
import { FileTypeService } from './file-type.service';

@Component({
  templateUrl: './file-type-delete-dialog.component.html',
})
export class FileTypeDeleteDialogComponent {
  fileType?: IFileType;

  constructor(protected fileTypeService: FileTypeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fileTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('fileTypeListModification');
      this.activeModal.close();
    });
  }
}
