package <%= packageName %>.internal.fileProcessing.fileNotification.processors;

import <%= packageName %>.internal.model.FileNotification;

public interface FileUploadProcessor<T> {

    T processFileUpload(T fileUpload, FileNotification fileNotification);
}
