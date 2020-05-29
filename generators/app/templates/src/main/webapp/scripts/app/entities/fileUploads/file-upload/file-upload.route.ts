import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFileUpload, FileUpload } from 'app/shared/model/fileUploads/file-upload.model';
import { FileUploadService } from './file-upload.service';
import { FileUploadComponent } from './file-upload.component';
import { FileUploadDetailComponent } from './file-upload-detail.component';
import { FileUploadUpdateComponent } from './file-upload-update.component';

@Injectable({ providedIn: 'root' })
export class FileUploadResolve implements Resolve<IFileUpload> {
  constructor(private service: FileUploadService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFileUpload> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((fileUpload: HttpResponse<FileUpload>) => {
          if (fileUpload.body) {
            return of(fileUpload.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FileUpload());
  }
}

export const fileUploadRoute: Routes = [
  {
    path: '',
    component: FileUploadComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams,
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'prepsGateApp.fileUploadsFileUpload.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FileUploadDetailComponent,
    resolve: {
      fileUpload: FileUploadResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'prepsGateApp.fileUploadsFileUpload.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FileUploadUpdateComponent,
    resolve: {
      fileUpload: FileUploadResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'prepsGateApp.fileUploadsFileUpload.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FileUploadUpdateComponent,
    resolve: {
      fileUpload: FileUploadResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'prepsGateApp.fileUploadsFileUpload.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
