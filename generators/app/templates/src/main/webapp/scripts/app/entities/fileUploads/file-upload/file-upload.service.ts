import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFileUpload } from 'app/shared/model/fileUploads/file-upload.model';

type EntityResponseType = HttpResponse<IFileUpload>;
type EntityArrayResponseType = HttpResponse<IFileUpload[]>;

@Injectable({ providedIn: 'root' })
export class FileUploadService {
  public resourceUrl = SERVER_API_URL + 'services/prepsmain/api/file-uploads';

  constructor(protected http: HttpClient) {}

  create(fileUpload: IFileUpload): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fileUpload);
    return this.http
      .post<IFileUpload>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fileUpload: IFileUpload): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fileUpload);
    return this.http
      .put<IFileUpload>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFileUpload>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFileUpload[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(fileUpload: IFileUpload): IFileUpload {
    const copy: IFileUpload = Object.assign({}, fileUpload, {
      periodFrom: fileUpload.periodFrom && fileUpload.periodFrom.isValid() ? fileUpload.periodFrom.format(DATE_FORMAT) : undefined,
      periodTo: fileUpload.periodTo && fileUpload.periodTo.isValid() ? fileUpload.periodTo.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.periodFrom = res.body.periodFrom ? moment(res.body.periodFrom) : undefined;
      res.body.periodTo = res.body.periodTo ? moment(res.body.periodTo) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((fileUpload: IFileUpload) => {
        fileUpload.periodFrom = fileUpload.periodFrom ? moment(fileUpload.periodFrom) : undefined;
        fileUpload.periodTo = fileUpload.periodTo ? moment(fileUpload.periodTo) : undefined;
      });
    }
    return res;
  }
}
