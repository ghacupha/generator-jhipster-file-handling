import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { PrepsGateTestModule } from '../../../../test.module';
import { FileTypeDetailComponent } from 'app/entities/fileUploads/file-type/file-type-detail.component';
import { FileType } from 'app/shared/model/fileUploads/file-type.model';

describe('Component Tests', () => {
  describe('FileType Management Detail Component', () => {
    let comp: FileTypeDetailComponent;
    let fixture: ComponentFixture<FileTypeDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ fileType: new FileType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PrepsGateTestModule],
        declarations: [FileTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(FileTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FileTypeDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load fileType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fileType).toEqual(jasmine.objectContaining({ id: 123 }));
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
