package <%= packageName %>.internal.model.sampleDataModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// todo optionally remove elasticsearch
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link CurrencyTable}.
 */
@Service
@Transactional
public class CurrencyTableServiceImpl implements CurrencyTableService {

    private final Logger log = LoggerFactory.getLogger(CurrencyTableServiceImpl.class);

    private final CurrencyTableRepository currencyTableRepository;

    private final CurrencyTableMapper currencyTableMapper;

    private final CurrencyTableSearchRepository currencyTableSearchRepository;

    public CurrencyTableServiceImpl(CurrencyTableRepository currencyTableRepository, CurrencyTableMapper currencyTableMapper, CurrencyTableSearchRepository currencyTableSearchRepository) {
        this.currencyTableRepository = currencyTableRepository;
        this.currencyTableMapper = currencyTableMapper;
        this.currencyTableSearchRepository = currencyTableSearchRepository;
    }

    /**
     * Save a currencyTable.
     *
     * @param currencyTableDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CurrencyTableDTO save(CurrencyTableDTO currencyTableDTO) {
        log.debug("Request to save CurrencyTable : {}", currencyTableDTO);
        CurrencyTable currencyTable = currencyTableMapper.toEntity(currencyTableDTO);
        currencyTable = currencyTableRepository.save(currencyTable);
        CurrencyTableDTO result = currencyTableMapper.toDto(currencyTable);
        currencyTableSearchRepository.save(currencyTable);
        return result;
    }

    /**
     * Get all the currencyTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CurrencyTableDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CurrencyTables");
        return currencyTableRepository.findAll(pageable)
            .map(currencyTableMapper::toDto);
    }


    /**
     * Get one currencyTable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CurrencyTableDTO> findOne(Long id) {
        log.debug("Request to get CurrencyTable : {}", id);
        return currencyTableRepository.findById(id)
            .map(currencyTableMapper::toDto);
    }

    /**
     * Delete the currencyTable by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CurrencyTable : {}", id);

        currencyTableRepository.deleteById(id);
        currencyTableSearchRepository.deleteById(id);
    }

    /**
     * Search for the currencyTable corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CurrencyTableDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CurrencyTables for query {}", query);
        return currencyTableSearchRepository.search(queryStringQuery(query), pageable)
            .map(currencyTableMapper::toDto);
    }
}
