import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PrepsGateTestModule } from '../../../../test.module';
import { FileTypeUpdateComponent } from 'app/entities/fileUploads/file-type/file-type-update.component';
import { FileTypeService } from 'app/entities/fileUploads/file-type/file-type.service';
import { FileType } from 'app/shared/model/fileUploads/file-type.model';

describe('Component Tests', () => {
  describe('FileType Management Update Component', () => {
    let comp: FileTypeUpdateComponent;
    let fixture: ComponentFixture<FileTypeUpdateComponent>;
    let service: FileTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PrepsGateTestModule],
        declarations: [FileTypeUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(FileTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FileTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FileTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FileType(123);
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
        const entity = new FileType();
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
