package <%= packageName %>.internal.fileProcessing.fileNotification.processors;

import <%= packageName %>.internal.fileProcessing.fileNotification.FileNotification;

public interface FileUploadProcessor<T> {

    T processFileUpload(T fileUpload, FileNotification fileNotification);
}
