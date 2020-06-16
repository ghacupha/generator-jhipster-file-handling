package <%= packageName %>.internal.model.sampleDataModel;

import <%= packageName %>.internal.service.BatchService;
import <%= packageName %>.service.dto.CurrencyTableDTO;
import <%= packageName %>.service.mapper.CurrencyTableMapper;
import <%= packageName %>.repository.CurrencyTableRepository;
import <%= packageName %>.repository.search.CurrencyTableSearchRepository;
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
