package io.github.deposits.app.service;

import io.github.deposits.domain.CurrencyTable;
import io.github.deposits.repository.CurrencyTableRepository;
import io.github.deposits.repository.search.CurrencyTableSearchRepository;
import io.github.deposits.service.dto.CurrencyTableDTO;
import io.github.deposits.service.mapper.CurrencyTableMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("currencyTableBatchService")
public class CurrencyTableBatchService implements BatchService<CurrencyTableDTO> {

    private final CurrencyTableMapper currencyTableMapper;
    private final CurrencyTableRepository currencyTableRepository;
    private final CurrencyTableSearchRepository currencyTableSearchRepository;

    public CurrencyTableBatchService(final CurrencyTableMapper currencyTableMapper, final CurrencyTableRepository currencyTableRepository,
                                     final CurrencyTableSearchRepository currencyTableSearchRepository) {
        this.currencyTableMapper = currencyTableMapper;
        this.currencyTableRepository = currencyTableRepository;
        this.currencyTableSearchRepository = currencyTableSearchRepository;
    }

    /**
     * Save an entity.
     *
     * @param entities entity to save.
     * @return the persisted entity.
     */
    @Override
    public List<CurrencyTableDTO> save(final List<CurrencyTableDTO> entities) {
        return currencyTableMapper.toDto(currencyTableRepository.saveAll(currencyTableMapper.toEntity(entities)));
    }

    /**
     * The above call only persists entities to the relations db repository for efficiency sake. Therefore to have it all in an index one needs to call this function
     */
    @Override
    public void index(final List<CurrencyTableDTO> entities) {

        currencyTableSearchRepository.saveAll(currencyTableMapper.toEntity(entities));
    }
}
