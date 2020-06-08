package io.github.deposits.app.service;

import io.github.deposits.repository.DepositAccountRepository;
import io.github.deposits.repository.search.DepositAccountSearchRepository;
import io.github.deposits.service.DepositAccountService;
import io.github.deposits.service.dto.DepositAccountDTO;
import io.github.deposits.service.mapper.DepositAccountMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * This class deals with really heavy stuff. Collection of data in the dozens of thousands per method call.
 * Therefore it is expected that a collection will be sunk into a relational database in one go.
 * Having pulled that off it will be necessary to update the search index, therefore
 * despite the fact that the index method parameter is a collection,
 * each of the items is added into an index through a parallel stream
 */
@Transactional
@Service("appDepositAccountService")
public class AppDepositAccountService implements DepositAccountService, DepositAccountBatchService {

    private final DepositAccountService depositAccountService;
    private final DepositAccountMapper depositAccountMapper;
    private final DepositAccountRepository depositAccountRepository;
    private final DepositAccountSearchRepository depositAccountSearchRepository;

    public AppDepositAccountService(final DepositAccountService depositAccountServiceDecorator, final DepositAccountMapper depositAccountMapper,
                                    final DepositAccountRepository depositAccountRepository, final DepositAccountSearchRepository depositAccountSearchRepository) {
        this.depositAccountService = depositAccountServiceDecorator;
        this.depositAccountMapper = depositAccountMapper;
        this.depositAccountRepository = depositAccountRepository;
        this.depositAccountSearchRepository = depositAccountSearchRepository;
    }

    /**
     * Save a depositAccount.
     *
     * @param depositAccounts the entity to save.
     * @return the persisted entity.
     */
    @Override
    public List<DepositAccountDTO> save(final List<DepositAccountDTO> depositAccounts) {
      //    depositAccountSearchRepository.saveAll(depositAccountMapper.toEntity(depositAccounts));
        return depositAccountMapper.toDto(depositAccountRepository.saveAll(depositAccountMapper.toEntity(depositAccounts)));
    }

    /**
     * The above call only persists deposit accounts to the relations db repository for efficiency sake. Therefore to have it all in an index one needs to call this function
     */
    @Override
    public void index(final List<DepositAccountDTO> depositAccounts) {

        depositAccountSearchRepository.saveAll(depositAccountMapper.toEntity(depositAccounts));
    }

    /**
     * Save a depositAccount.
     *
     * @param depositAccountDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DepositAccountDTO save(final DepositAccountDTO depositAccountDTO) {
        return depositAccountService.save(depositAccountDTO);
    }

    /**
     * Get all the depositAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<DepositAccountDTO> findAll(final Pageable pageable) {
        return depositAccountService.findAll(pageable);
    }

    /**
     * Get the "id" depositAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<DepositAccountDTO> findOne(final Long id) {
        return depositAccountService.findOne(id);
    }

    /**
     * Delete the "id" depositAccount.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(final Long id) {
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
    public Page<DepositAccountDTO> search(final String query, final Pageable pageable) {
        return depositAccountService.search(query, pageable);
    }
}
