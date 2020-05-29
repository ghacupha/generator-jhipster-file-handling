import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IFileType, FileType } from 'app/shared/model/fileUploads/file-type.model';
import { FileTypeService } from './file-type.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'gha-file-type-update',
  templateUrl: './file-type-update.component.html',
})
export class FileTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fileTypeName: [null, [Validators.required]],
    fileMediumType: [null, [Validators.required]],
    description: [],
    fileTemplate: [],
    fileTemplateContentType: [],
    fileType: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected fileTypeService: FileTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileType }) => {
      this.updateForm(fileType);
    });
  }

  updateForm(fileType: IFileType): void {
    this.editForm.patchValue({
      id: fileType.id,
      fileTypeName: fileType.fileTypeName,
      fileMediumType: fileType.fileMediumType,
      description: fileType.description,
      fileTemplate: fileType.fileTemplate,
      fileTemplateContentType: fileType.fileTemplateContentType,
      fileType: fileType.fileType,
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
    const fileType = this.createFromForm();
    if (fileType.id !== undefined) {
      this.subscribeToSaveResponse(this.fileTypeService.update(fileType));
    } else {
      this.subscribeToSaveResponse(this.fileTypeService.create(fileType));
    }
  }

  private createFromForm(): IFileType {
    return {
      ...new FileType(),
      id: this.editForm.get(['id'])!.value,
      fileTypeName: this.editForm.get(['fileTypeName'])!.value,
      fileMediumType: this.editForm.get(['fileMediumType'])!.value,
      description: this.editForm.get(['description'])!.value,
      fileTemplateContentType: this.editForm.get(['fileTemplateContentType'])!.value,
      fileTemplate: this.editForm.get(['fileTemplate'])!.value,
      fileType: this.editForm.get(['fileType'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFileType>>): void {
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
