package io.github.deposits.app.service;

import io.github.deposits.service.dto.DepositAccountDTO;

import java.util.List;


public interface DepositAccountBatchService extends BatchService<DepositAccountDTO> {

    /**
     * Save a depositAccount.
     *
     * @param depositAccounts entity to save.
     * @return the persisted entity.
     */
    List<DepositAccountDTO> save(List<DepositAccountDTO> depositAccounts);

    /**
     * The above call only persists deposit accounts to the relations db repository
     * for efficiency sake.
     * Therefore to have it all in an index one needs to call this function
     * @param depositAccounts
     * @return
     */
    void index(List<DepositAccountDTO> depositAccounts);
}
