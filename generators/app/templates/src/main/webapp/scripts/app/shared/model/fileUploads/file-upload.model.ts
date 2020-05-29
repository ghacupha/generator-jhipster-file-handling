import { Moment } from 'moment';

export interface IFileUpload {
  id?: number;
  description?: string;
  fileName?: string;
  periodFrom?: Moment;
  periodTo?: Moment;
  fileTypeId?: number;
  dataFileContentType?: string;
  dataFile?: any;
  uploadSuccessful?: boolean;
  uploadProcessed?: boolean;
  uploadToken?: string;
}

export class FileUpload implements IFileUpload {
  constructor(
    public id?: number,
    public description?: string,
    public fileName?: string,
    public periodFrom?: Moment,
    public periodTo?: Moment,
    public fileTypeId?: number,
    public dataFileContentType?: string,
    public dataFile?: any,
    public uploadSuccessful?: boolean,
    public uploadProcessed?: boolean,
    public uploadToken?: string
  ) {
    this.uploadSuccessful = this.uploadSuccessful || false;
    this.uploadProcessed = this.uploadProcessed || false;
  }
}
