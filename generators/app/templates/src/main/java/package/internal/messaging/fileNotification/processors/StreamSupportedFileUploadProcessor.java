package io.github.deposits.app.messaging.fileNotification.processors;

import io.github.deposits.app.Mapping;
import io.github.deposits.app.excel.ExcelFileDeserializer;
import io.github.deposits.app.messaging.fileNotification.FileNotification;
import io.github.deposits.app.messaging.platform.MessageService;
import io.github.deposits.app.messaging.platform.TokenizableMessage;
import io.github.deposits.domain.enumeration.FileModelType;
import io.github.deposits.service.dto.FileUploadDTO;
import io.github.deposits.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

import static io.github.deposits.app.AppConstants.ENQUEUED_TOKENS;

/**
 * This is a processor which is determined to processe files by sending them into a queue.
 * The object can then be initialized in a container and providing all the parameters.
 * Once the file is received it is deserialized by an appropriate excel-file-deserializer
 * and then sent into a queue by the appropriate message-service as configured.
 * It is not currently in use but it is hoped that it can be used for future excel file based
 * data uploads
 */
@Slf4j
@Transactional
public class StreamSupportedFileUploadProcessor<EVM> implements FileUploadProcessor<FileUploadDTO> {

    private final MessageService<TokenizableMessage<String>, MessageTokenDTO> schemeTableMessageService;
    private final ExcelFileDeserializer<EVM> excelFileDeserializer;
    private final Mapping<EVM, TokenizableMessage<String>> schemeTableMTOMapping;
    private final FileModelType fileModelType;

    public StreamSupportedFileUploadProcessor(final MessageService<TokenizableMessage<String>, MessageTokenDTO> schemeTableMessageService,
                                              final ExcelFileDeserializer<EVM> excelFileDeserializer, final Mapping<EVM, TokenizableMessage<String>> schemeTableMTOMapping,
                                              final FileModelType fileModelType) {
        this.schemeTableMessageService = schemeTableMessageService;
        this.excelFileDeserializer = excelFileDeserializer;
        this.schemeTableMTOMapping = schemeTableMTOMapping;
        this.fileModelType = fileModelType;
    }

    @Override
    public FileUploadDTO processFileUpload(final FileUploadDTO fileUpload, FileNotification fileNotification) {
        log.debug("Received file-upload for processing :{} with notification :{}", fileUpload, fileNotification);

        // Only works on scheme tables
        if (fileNotification.getFileModelType() == fileModelType) {
            log.debug("File-upload type confirmed commencing process...");
            AtomicInteger processCounter = new AtomicInteger();

            // TODO pull the correct file from the database
            log.info("Pulling the scheme data file name {} from DB", fileUpload.getFileName());

            // @formatter:off
            excelFileDeserializer.deserialize(fileUpload.getDataFile())
                                 .stream()
                                 .map(schemeTableMTOMapping::toValue2)
                                 .map(schemeTableMessageService::sendMessage)
                                 .peek(i -> {processCounter.incrementAndGet();})
                                 .map(MessageTokenDTO::getTokenValue)
                                 .forEach(ENQUEUED_TOKENS::add);
            // @formatter:on

            log.info("{} scheme-table items enqueued...", processCounter.get());

            fileUpload.setUploadSuccessful(true);
            fileUpload.setUploadProcessed(true);
        }
        log.debug("File upload inconsistent with the data model supported by this processor");

        return fileUpload;
    }
}
