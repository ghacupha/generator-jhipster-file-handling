package <%= packageName %>.internal.model.sampleDataModel;

// todo optionally remove elasticsearch
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link CurrencyTable} entity.
 */
public interface CurrencyTableSearchRepository extends ElasticsearchRepository<CurrencyTable, Long> {
}
