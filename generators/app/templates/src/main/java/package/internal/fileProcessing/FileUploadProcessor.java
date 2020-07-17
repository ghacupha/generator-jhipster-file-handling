package <%= packageName %>.internal.fileProcessing;

import <%= packageName %>.internal.model.FileNotification;

public interface FileUploadProcessor<T> {

    T processFileUpload(T fileUpload, FileNotification fileNotification);
}
