package io.github.deposits.app.messaging.fileNotification;

import io.github.deposits.app.messaging.fileNotification.processors.FileUploadProcessorChain;
import io.github.deposits.app.messaging.platform.MuteListener;
import io.github.deposits.service.FileUploadService;
import io.github.deposits.service.dto.FileUploadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.github.deposits.app.AppConstants.PROCESSED_TOKENS;

/**
 * This class was transactional but such behavior is now regarded with careful caution as triggering that may lead to upload of the same file twice. We can't have that can we?
 *
 * The listener attends to all files and sends each into a chain for processing
 */
@Slf4j
@Service
public class FileNotificationListener implements MuteListener<FileNotification> {

    private final FileUploadService fileUploadService;
    private final FileUploadProcessorChain fileUploadProcessorChain;

    public FileNotificationListener(final FileUploadService fileUploadService, final FileUploadProcessorChain fileUploadProcessorChain) {
        this.fileUploadService = fileUploadService;
        this.fileUploadProcessorChain = fileUploadProcessorChain;
    }

    @StreamListener(FileNotificationStreams.INPUT)
    public void handleMessage(@Payload FileNotification fileNotification) {
        log.info("File notification received for: {}", fileNotification.getFilename());

        FileUploadDTO fileUpload =
            fileUploadService.findOne(Long.parseLong(fileNotification.getFileId())).orElseThrow(() -> new IllegalArgumentException("Id # : " + fileNotification.getFileId() + " does not exist"));
        log.debug("FileUploadDTO object fetched from DB with id: {}", fileUpload.getId());
        if (!PROCESSED_TOKENS.contains(fileNotification.getMessageToken())) {
            log.debug("Processing message with token {}", fileNotification.getMessageToken());
            List<FileUploadDTO> processedFiles = fileUploadProcessorChain.apply(fileUpload, fileNotification);
            fileUpload.setUploadProcessed(true);
            fileUpload.setUploadSuccessful(true);
            // Explicitly persist new status
            fileUploadService.save(fileUpload);
            PROCESSED_TOKENS.add(fileNotification.getMessageToken());
        } else {
            log.info("Skipped upload of processed files {}", fileNotification.getFilename());
        }
    }
}
