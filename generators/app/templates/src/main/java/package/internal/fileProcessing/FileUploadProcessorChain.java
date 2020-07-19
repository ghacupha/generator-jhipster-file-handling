package <%= packageName %>.internal.fileProcessing;

import com.google.common.collect.ImmutableList;
import <%= packageName %>.internal.model.FileNotification;
import <%= packageName %>.service.dto.<%= classNamesPrefix %>FileUploadDTO;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The idea is to have all processor in a class and apply them to a file upload
 */
public class FileUploadProcessorChain {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FileUploadProcessorChain.class);
    private final List<FileUploadProcessor<<%= classNamesPrefix %>FileUploadDTO>> fileUploadProcessors;

    public FileUploadProcessorChain(final List<FileUploadProcessor<<%= classNamesPrefix %>FileUploadDTO>> fileUploadProcessors) {
        this.fileUploadProcessors = fileUploadProcessors;
    }

    public FileUploadProcessorChain() {
        this.fileUploadProcessors = new CopyOnWriteArrayList<>();
    }

    public void addProcessor(FileUploadProcessor<<%= classNamesPrefix %>FileUploadDTO> fileUploadProcessor) {
        log.info("Adding new file-upload processor {}", fileUploadProcessor);
        this.fileUploadProcessors.add(fileUploadProcessor);
    }

    public List<<%= classNamesPrefix %>FileUploadDTO> apply(<%= classNamesPrefix %>FileUploadDTO fileUploadDTO, FileNotification fileNotification) {
        log.debug("Applying {} file upload processors to file-upload {} with notification {}", this.fileUploadProcessors.size(), fileUploadDTO, fileNotification);
        return fileUploadProcessors.stream().map(processor -> processor.processFileUpload(fileUploadDTO, fileNotification)).collect(ImmutableList.toImmutableList());
    }
}
