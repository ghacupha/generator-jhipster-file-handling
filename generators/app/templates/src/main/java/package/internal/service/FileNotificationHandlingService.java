package io.github.currencies.internal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.currencies.domain.CurrencyMainMessageToken;
import io.github.currencies.internal.batch.processors.FileUploadProcessorChain;
import io.github.currencies.internal.model.FileNotification;
import io.github.currencies.internal.util.TokenGenerator;
import io.github.currencies.service.CurrencyMainFileUploadService;
import io.github.currencies.service.CurrencyMainMessageTokenService;
import io.github.currencies.service.dto.CurrencyMainFileUploadDTO;
import io.github.currencies.service.dto.CurrencyMainMessageTokenDTO;
import io.github.currencies.service.mapper.CurrencyMainMessageTokenMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.github.currencies.internal.AppConstants.PROCESSED_TOKENS;

/**
 * This is a service that handles file-notification asynchronously.
 *
 */
@Service("fileNotificationHandlingService")
public class FileNotificationHandlingService implements HandlingService<FileNotification> {

    public static Logger log = LoggerFactory.getLogger(FileNotificationHandlingService.class);

    private final TokenGenerator tokenGenerator;
    private final CurrencyMainMessageTokenService messageTokenService;
    private final CurrencyMainMessageTokenMapper messageTokenMapper;
    private final CurrencyMainFileUploadService fileUploadService;
    private final FileUploadProcessorChain fileUploadProcessorChain;

    public FileNotificationHandlingService(TokenGenerator tokenGenerator, CurrencyMainMessageTokenService messageTokenService, CurrencyMainMessageTokenMapper messageTokenMapper, CurrencyMainFileUploadService fileUploadService, FileUploadProcessorChain fileUploadProcessorChain) {
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
        CurrencyMainMessageToken messageToken = new CurrencyMainMessageToken()
            .tokenValue(token)
            .description(payload.getDescription())
            .timeSent(timestamp);
        // @formatter:on

        if (messageToken != null) {
            payload.setMessageToken(messageToken.getTokenValue());
        }

        CurrencyMainFileUploadDTO fileUpload =
            fileUploadService.findOne(Long.parseLong(payload.getFileId())).orElseThrow(() -> new IllegalArgumentException("Id # : " + payload.getFileId() + " does not exist"));

        log.debug("CurrencyMainFileUploadDTO object fetched from DB with id: {}", fileUpload.getId());
        if (!PROCESSED_TOKENS.contains(payload.getMessageToken())) {
            log.debug("Processing message with token {}", payload.getMessageToken());
            List<CurrencyMainFileUploadDTO> processedFiles = fileUploadProcessorChain.apply(fileUpload, payload);
            fileUpload.setUploadProcessed(true);
            fileUpload.setUploadSuccessful(true);
            fileUpload.setUploadToken(token);
            // Explicitly persist new status
            fileUploadService.save(fileUpload);
            PROCESSED_TOKENS.add(payload.getMessageToken());
        } else {
            log.info("Skipped upload of processed files {}", payload.getFilename());
        }

        CurrencyMainMessageTokenDTO dto = messageTokenService.save(messageTokenMapper.toDto(messageToken));
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
