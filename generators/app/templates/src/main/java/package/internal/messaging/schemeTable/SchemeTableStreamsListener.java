package io.github.deposits.app.messaging.schemeTable;

import io.github.deposits.app.Mapping;
import io.github.deposits.app.messaging.platform.MuteListener;
import io.github.deposits.service.SchemeTableService;
import io.github.deposits.service.dto.SchemeTableDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.github.deposits.app.AppConstants.ENQUEUED_TOKENS;
import static io.github.deposits.app.AppConstants.PROCESSED_TOKENS;

/**
 * This service will listen for SchemeTableMTO in the queue and persist in the data sink
 */
@Slf4j
@Transactional
@Service
public class SchemeTableStreamsListener implements MuteListener<SchemeTableMTO> {

    private final SchemeTableService schemeTableService;
    private final Mapping<SchemeTableDTO, SchemeTableMTO> mtoMapping;

    public SchemeTableStreamsListener(final SchemeTableService schemeTableService, final Mapping<SchemeTableDTO, SchemeTableMTO> schemeTableDTOMTOMapping) {
        this.schemeTableService = schemeTableService;
        this.mtoMapping = schemeTableDTOMTOMapping;
    }

    @Override
    @StreamListener(SchemeTableStreams.INPUT)
    public void handleMessage(@Payload SchemeTableMTO schemeTableMTO) {
        log.debug("Scheme-table item : {} received for processing", schemeTableMTO);

        // Am trying to avoid transaction based duplication nonsense
        if (!PROCESSED_TOKENS.contains(schemeTableMTO.getMessageToken())) {
            schemeTableService.save(mtoMapping.toValue1(schemeTableMTO));
            PROCESSED_TOKENS.add(schemeTableMTO.getMessageToken());
        }

        // Check if all enqueued items have been processed
        if (PROCESSED_TOKENS.containsAll(ENQUEUED_TOKENS)) {
           // TODO Trigger some action, notify a watching client, or log it or mark file as fully enqueued
        }
    }
}
