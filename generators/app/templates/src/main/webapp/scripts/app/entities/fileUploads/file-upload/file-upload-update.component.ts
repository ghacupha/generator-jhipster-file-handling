import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IFileUpload, FileUpload } from 'app/shared/model/fileUploads/file-upload.model';
import { FileUploadService } from './file-upload.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'gha-file-upload-update',
  templateUrl: './file-upload-update.component.html',
})
export class FileUploadUpdateComponent implements OnInit {
  isSaving = false;
  periodFromDp: any;
  periodToDp: any;

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.required]],
    fileName: [null, [Validators.required]],
    periodFrom: [],
    periodTo: [],
    fileTypeId: [null, [Validators.required]],
    dataFile: [null, [Validators.required]],
    dataFileContentType: [],
    uploadSuccessful: [],
    uploadProcessed: [],
    uploadToken: [null, []],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected fileUploadService: FileUploadService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileUpload }) => {
      this.updateForm(fileUpload);
    });
  }

  updateForm(fileUpload: IFileUpload): void {
    this.editForm.patchValue({
      id: fileUpload.id,
      description: fileUpload.description,
      fileName: fileUpload.fileName,
      periodFrom: fileUpload.periodFrom,
      periodTo: fileUpload.periodTo,
      fileTypeId: fileUpload.fileTypeId,
      dataFile: fileUpload.dataFile,
      dataFileContentType: fileUpload.dataFileContentType,
      uploadSuccessful: fileUpload.uploadSuccessful,
      uploadProcessed: fileUpload.uploadProcessed,
      uploadToken: fileUpload.uploadToken,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('prepsGateApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fileUpload = this.createFromForm();
    if (fileUpload.id !== undefined) {
      this.subscribeToSaveResponse(this.fileUploadService.update(fileUpload));
    } else {
      this.subscribeToSaveResponse(this.fileUploadService.create(fileUpload));
    }
  }

  private createFromForm(): IFileUpload {
    return {
      ...new FileUpload(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      fileName: this.editForm.get(['fileName'])!.value,
      periodFrom: this.editForm.get(['periodFrom'])!.value,
      periodTo: this.editForm.get(['periodTo'])!.value,
      fileTypeId: this.editForm.get(['fileTypeId'])!.value,
      dataFileContentType: this.editForm.get(['dataFileContentType'])!.value,
      dataFile: this.editForm.get(['dataFile'])!.value,
      uploadSuccessful: this.editForm.get(['uploadSuccessful'])!.value,
      uploadProcessed: this.editForm.get(['uploadProcessed'])!.value,
      uploadToken: this.editForm.get(['uploadToken'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFileUpload>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
