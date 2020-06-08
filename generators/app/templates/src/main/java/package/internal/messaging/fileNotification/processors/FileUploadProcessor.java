package <%= packageName %>.internal.messaging.fileNotification.processors;

import <%= packageName %>.internal.messaging.fileNotification.FileNotification;

public interface FileUploadProcessor<T> {

    T processFileUpload(T fileUpload, FileNotification fileNotification);
}
