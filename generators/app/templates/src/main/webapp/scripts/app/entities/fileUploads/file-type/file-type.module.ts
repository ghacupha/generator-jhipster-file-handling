import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PrepsGateSharedModule } from 'app/shared/shared.module';
import { FileTypeComponent } from './file-type.component';
import { FileTypeDetailComponent } from './file-type-detail.component';
import { FileTypeUpdateComponent } from './file-type-update.component';
import { FileTypeDeleteDialogComponent } from './file-type-delete-dialog.component';
import { fileTypeRoute } from './file-type.route';

@NgModule({
  imports: [PrepsGateSharedModule, RouterModule.forChild(fileTypeRoute)],
  declarations: [FileTypeComponent, FileTypeDetailComponent, FileTypeUpdateComponent, FileTypeDeleteDialogComponent],
  entryComponents: [FileTypeDeleteDialogComponent],
})
export class PrepsMainFileTypeModule {}
