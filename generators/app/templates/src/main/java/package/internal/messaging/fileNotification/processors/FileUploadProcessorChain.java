package <%= packageName %>.internal.messaging.fileNotification.processors;

import com.google.common.collect.ImmutableList;
import <%= packageName %>.internal.messaging.fileNotification.FileNotification;
import <%= packageName %>.service.dto.FileUploadDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The idea is to have all processor in a class and apply them to a file upload
 */
@Slf4j
public class FileUploadProcessorChain {

    private final List<FileUploadProcessor<FileUploadDTO>> fileUploadProcessors;

    public FileUploadProcessorChain(final List<FileUploadProcessor<FileUploadDTO>> fileUploadProcessors) {
        this.fileUploadProcessors = fileUploadProcessors;
    }

    public FileUploadProcessorChain() {
        this.fileUploadProcessors = new CopyOnWriteArrayList<>();
    }

    public void addProcessor(FileUploadProcessor<FileUploadDTO> fileUploadProcessor) {
        log.info("Adding new file-upload processor {}", fileUploadProcessor);
        this.fileUploadProcessors.add(fileUploadProcessor);
    }

    public List<FileUploadDTO> apply(FileUploadDTO fileUploadDTO, FileNotification fileNotification) {
        log.debug("Applying {} file upload processors to file-upload {} with notification {}", this.fileUploadProcessors.size(), fileUploadDTO, fileNotification);
        return fileUploadProcessors.stream().map(processor -> processor.processFileUpload(fileUploadDTO, fileNotification)).collect(ImmutableList.toImmutableList());
    }
}
