package io.github.deposits.app.messaging.fileNotification.processors;

import io.github.deposits.app.messaging.fileNotification.FileNotification;

public interface FileUploadProcessor<T> {

    T processFileUpload(T fileUpload, FileNotification fileNotification);
}
