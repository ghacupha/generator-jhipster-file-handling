package io.github.deposits.app.service;

import io.github.deposits.service.DepositAccountService;
import io.github.deposits.service.dto.DepositAccountDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("depositAccountServiceDecorator")
@Transactional
public class DepositAccountServiceDecorator implements DepositAccountService {


    private final DepositAccountService depositAccountService;

    public DepositAccountServiceDecorator(@Qualifier("depositAccountService") final DepositAccountService depositAccountService) {
        this.depositAccountService = depositAccountService;
    }

    /**
     * Save a depositAccount.
     *
     * @param depositAccountDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DepositAccountDTO save(DepositAccountDTO depositAccountDTO) {
        return depositAccountService.save(depositAccountDTO);
    }

    /**
     * Get all the depositAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DepositAccountDTO> findAll(Pageable pageable) {
        return depositAccountService.findAll(pageable);
    }


    /**
     * Get one depositAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DepositAccountDTO> findOne(Long id) {
        return depositAccountService.findOne(id);
    }

    /**
     * Delete the depositAccount by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        depositAccountService.delete(id);
    }

    /**
     * Search for the depositAccount corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DepositAccountDTO> search(String query, Pageable pageable) {
        return depositAccountService.search(query, pageable);
    }
}
