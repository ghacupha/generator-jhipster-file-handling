package io.github.deposits.app.messaging.depositAccount;

import com.google.common.collect.ImmutableList;
import io.github.deposits.app.Mapping;
import io.github.deposits.app.messaging.platform.MuteListener;
import io.github.deposits.app.service.DepositAccountBatchService;
import io.github.deposits.service.DepositAccountService;
import io.github.deposits.service.dto.DepositAccountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static io.github.deposits.app.AppConstants.MAX_DEPOSIT_UPDATE_SIZE;

/**
 * This listener receives deposit-account-mto items from the queue and persists them in the db and the elastic search index one by one.
 * This object needs to have a way to batch multiple items for persistence so that a lot of time is not wasted while persisting single
 * items at a time. The time would just not be enough.
 */
@Service
@Slf4j
public class DepositAccountStreamsListener implements MuteListener<DepositAccountMTO> {

    private final DepositAccountService depositAccountService;
    private final Mapping<DepositAccountMTO, DepositAccountDTO> depositAccountMTODTOMapping;
    private final DepositAccountBatchService depositAccountBatchService;

    private final List<DepositAccountMTO> rawDepositAccounts;

    public DepositAccountStreamsListener(final DepositAccountService depositAccountService, final Mapping<DepositAccountMTO, DepositAccountDTO> depositAccountMTODTOMapping, final DepositAccountBatchService depositAccountBatchService) {
        this.depositAccountService = depositAccountService;
        this.depositAccountMTODTOMapping = depositAccountMTODTOMapping;
        this.depositAccountBatchService = depositAccountBatchService;
        rawDepositAccounts = new CopyOnWriteArrayList<>();
    }

    @Override
    @StreamListener(DepositAccountStreams.INPUT)
    public void handleMessage(@Payload DepositAccountMTO depositAccountMTO) {

        log.trace("Deposit-Account item : {} received for processing", depositAccountMTO);
//        depositAccountService.save(depositAccountMTODTOMapping.toValue2(depositAccountMTO));

        rawDepositAccounts.add(depositAccountMTO);

        // TODO modify the algorithm to save the last batch of items less than 5000
        while(rawDepositAccounts.size() >= MAX_DEPOSIT_UPDATE_SIZE) {

            // copy list
            List<DepositAccountMTO> depositAccountsForPersistence = ImmutableList.copyOf(rawDepositAccounts);

            // remove copied items
            rawDepositAccounts.removeAll(depositAccountsForPersistence);
//            rawDepositAccounts.clear();

            List<DepositAccountDTO> depositAccountsForIndexing = depositAccountBatchService.save(depositAccountMTODTOMapping.toValue2(depositAccountsForPersistence));

            depositAccountBatchService.index(depositAccountsForIndexing);
        }
    }
}
