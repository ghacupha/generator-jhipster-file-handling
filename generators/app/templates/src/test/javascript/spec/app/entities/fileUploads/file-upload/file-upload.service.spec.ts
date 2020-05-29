import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { FileUploadService } from 'app/entities/fileUploads/file-upload/file-upload.service';
import { IFileUpload, FileUpload } from 'app/shared/model/fileUploads/file-upload.model';

describe('Service Tests', () => {
  describe('FileUpload Service', () => {
    let injector: TestBed;
    let service: FileUploadService;
    let httpMock: HttpTestingController;
    let elemDefault: IFileUpload;
    let expectedResult: IFileUpload | IFileUpload[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(FileUploadService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new FileUpload(0, 'AAAAAAA', 'AAAAAAA', currentDate, currentDate, 0, 'image/png', 'AAAAAAA', false, false, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            periodFrom: currentDate.format(DATE_FORMAT),
            periodTo: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FileUpload', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            periodFrom: currentDate.format(DATE_FORMAT),
            periodTo: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            periodFrom: currentDate,
            periodTo: currentDate,
          },
          returnedFromService
        );

        service.create(new FileUpload()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FileUpload', () => {
        const returnedFromService = Object.assign(
          {
            description: 'BBBBBB',
            fileName: 'BBBBBB',
            periodFrom: currentDate.format(DATE_FORMAT),
            periodTo: currentDate.format(DATE_FORMAT),
            fileTypeId: 1,
            dataFile: 'BBBBBB',
            uploadSuccessful: true,
            uploadProcessed: true,
            uploadToken: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            periodFrom: currentDate,
            periodTo: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FileUpload', () => {
        const returnedFromService = Object.assign(
          {
            description: 'BBBBBB',
            fileName: 'BBBBBB',
            periodFrom: currentDate.format(DATE_FORMAT),
            periodTo: currentDate.format(DATE_FORMAT),
            fileTypeId: 1,
            dataFile: 'BBBBBB',
            uploadSuccessful: true,
            uploadProcessed: true,
            uploadToken: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            periodFrom: currentDate,
            periodTo: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FileUpload', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
