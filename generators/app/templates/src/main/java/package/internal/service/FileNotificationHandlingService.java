package <%= packageName %>.internal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import <%= packageName %>.domain.<%= classNamesPrefix %>MessageToken;
import <%= packageName %>.internal.fileProcessing.fileNotification.FileNotification;
import <%= packageName %>.internal.fileProcessing.fileNotification.processors.FileUploadProcessorChain;
import <%= packageName %>.internal.util.TokenGenerator;
import <%= packageName %>.service.<%= classNamesPrefix %>FileUploadService;
import <%= packageName %>.service.<%= classNamesPrefix %>MessageTokenService;
import <%= packageName %>.service.dto.<%= classNamesPrefix %>FileUploadDTO;
import <%= packageName %>.service.dto.<%= classNamesPrefix %>MessageTokenDTO;
import <%= packageName %>.service.mapper.<%= classNamesPrefix %>MessageTokenMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

import static <%= packageName %>.internal.AppConstants.PROCESSED_TOKENS;

/**
 * This is a service that handles file-notification asynchronously.
 *
 */
@Service("fileNotificationHandlingService")
public class FileNotificationHandlingService implements HandlingService<FileNotification> {

    public static Logger log = LoggerFactory.getLogger(FileNotificationHandlingService.class);

    private final TokenGenerator tokenGenerator;
    private final <%= classNamesPrefix %>MessageTokenService messageTokenService;
    private final <%= classNamesPrefix %>MessageTokenMapper messageTokenMapper;
    private final <%= classNamesPrefix %>FileUploadService fileUploadService;
    private final FileUploadProcessorChain fileUploadProcessorChain;

    public FileNotificationHandlingService(TokenGenerator tokenGenerator, <%= classNamesPrefix %>MessageTokenService messageTokenService, <%= classNamesPrefix %>MessageTokenMapper messageTokenMapper, <%= classNamesPrefix %>FileUploadService fileUploadService, FileUploadProcessorChain fileUploadProcessorChain) {
        this.tokenGenerator = tokenGenerator;
        this.messageTokenService = messageTokenService;
        this.messageTokenMapper = messageTokenMapper;
        this.fileUploadService = fileUploadService;
        this.fileUploadProcessorChain = fileUploadProcessorChain;
    }

    @Override
    @Async
    public void handle(FileNotification payload) {

        log.info("File notification received for: {}", payload.getFilename());

        // Generate token before getting timestamps
        String token  = getToken(payload);

        long timestamp = System.currentTimeMillis();
        payload.setTimestamp(timestamp);

        // @formatter:off
        <%= classNamesPrefix %>MessageToken messageToken = new <%= classNamesPrefix %>MessageToken()
            .tokenValue(token)
            .description(payload.getDescription())
            .timeSent(timestamp);
        // @formatter:on

        if (messageToken != null) {
            payload.setMessageToken(messageToken.getTokenValue());
        }

        <%= classNamesPrefix %>FileUploadDTO fileUpload =
            fileUploadService.findOne(Long.parseLong(payload.getFileId())).orElseThrow(() -> new IllegalArgumentException("Id # : " + payload.getFileId() + " does not exist"));

        log.debug("FileUploadDTO object fetched from DB with id: {}", fileUpload.getId());
        if (!PROCESSED_TOKENS.contains(payload.getMessageToken())) {
            log.debug("Processing message with token {}", payload.getMessageToken());
            List<<%= classNamesPrefix %>FileUploadDTO> processedFiles = fileUploadProcessorChain.apply(fileUpload, payload);
            fileUpload.setUploadProcessed(true);
            fileUpload.setUploadSuccessful(true);
            fileUpload.setUploadToken(token);
            // Explicitly persist new status
            fileUploadService.save(fileUpload);
            PROCESSED_TOKENS.add(payload.getMessageToken());
        } else {
            log.info("Skipped upload of processed files {}", payload.getFilename());
        }

        <%= classNamesPrefix %>MessageTokenDTO dto = messageTokenService.save(messageTokenMapper.toDto(messageToken));
        dto.setContentFullyEnqueued(true);

    }

    private String getToken(FileNotification payload) {
        String token = "";
        try {
            token = tokenGenerator.md5Digest(payload);
        } catch (JsonProcessingException e) {
            log.error("The service has failed to create a message-token and has been aborted : ", e);
        }
        return token;
    }
}
