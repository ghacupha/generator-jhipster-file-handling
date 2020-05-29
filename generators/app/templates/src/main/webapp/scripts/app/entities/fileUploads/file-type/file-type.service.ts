import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFileType } from 'app/shared/model/fileUploads/file-type.model';

type EntityResponseType = HttpResponse<IFileType>;
type EntityArrayResponseType = HttpResponse<IFileType[]>;

@Injectable({ providedIn: 'root' })
export class FileTypeService {
  public resourceUrl = SERVER_API_URL + 'services/prepsmain/api/file-types';

  constructor(protected http: HttpClient) {}

  create(fileType: IFileType): Observable<EntityResponseType> {
    return this.http.post<IFileType>(this.resourceUrl, fileType, { observe: 'response' });
  }

  update(fileType: IFileType): Observable<EntityResponseType> {
    return this.http.put<IFileType>(this.resourceUrl, fileType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFileType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFileType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
