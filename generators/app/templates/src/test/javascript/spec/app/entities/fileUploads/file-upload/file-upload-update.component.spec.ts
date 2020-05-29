import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PrepsGateTestModule } from '../../../../test.module';
import { FileUploadUpdateComponent } from 'app/entities/fileUploads/file-upload/file-upload-update.component';
import { FileUploadService } from 'app/entities/fileUploads/file-upload/file-upload.service';
import { FileUpload } from 'app/shared/model/fileUploads/file-upload.model';

describe('Component Tests', () => {
  describe('FileUpload Management Update Component', () => {
    let comp: FileUploadUpdateComponent;
    let fixture: ComponentFixture<FileUploadUpdateComponent>;
    let service: FileUploadService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PrepsGateTestModule],
        declarations: [FileUploadUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(FileUploadUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FileUploadUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FileUploadService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FileUpload(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new FileUpload();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
