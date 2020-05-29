import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { PrepsGateTestModule } from '../../../../test.module';
import { FileUploadDetailComponent } from 'app/entities/fileUploads/file-upload/file-upload-detail.component';
import { FileUpload } from 'app/shared/model/fileUploads/file-upload.model';

describe('Component Tests', () => {
  describe('FileUpload Management Detail Component', () => {
    let comp: FileUploadDetailComponent;
    let fixture: ComponentFixture<FileUploadDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ fileUpload: new FileUpload(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PrepsGateTestModule],
        declarations: [FileUploadDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(FileUploadDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FileUploadDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load fileUpload on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fileUpload).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
