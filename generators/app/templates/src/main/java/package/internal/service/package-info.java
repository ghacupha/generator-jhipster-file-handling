/**
 * Package contains services called in a typical batch process workflow
 * 
 * For instance if we have a service for a SchemeTable entity it might look like so : 
 * 
 * {@code 
 * 
        package <%= packageName %>.internal.service;

        import io.github.deposits.repository.SchemeTableRepository;
        import io.github.deposits.repository.search.SchemeTableSearchRepository;
        import io.github.deposits.service.dto.SchemeTableDTO;
        import io.github.deposits.service.mapper.SchemeTableMapper;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;

        import java.util.List;

        @Transactional
        @Service
        public class SchemeTableBatchService implements BatchService<SchemeTableDTO> {

            private final SchemeTableMapper schemeTableMapper;
            private final SchemeTableRepository schemeTableRepository;
            private final SchemeTableSearchRepository schemeTableSearchRepository;

            public SchemeTableBatchService(final SchemeTableMapper schemeTableMapper, final SchemeTableRepository schemeTableRepository, final SchemeTableSearchRepository schemeTableSearchRepository) {
                this.schemeTableMapper = schemeTableMapper;
                this.schemeTableRepository = schemeTableRepository;
                this.schemeTableSearchRepository = schemeTableSearchRepository;
            }

            @Override
            public List<SchemeTableDTO> save(final List<SchemeTableDTO> entities) {
                return schemeTableMapper.toDto(schemeTableRepository.saveAll(schemeTableMapper.toEntity(entities)));
            }

            @Override
            public void index(final List<SchemeTableDTO> entities) {

                this.schemeTableSearchRepository.saveAll(schemeTableMapper.toEntity(entities));
            }
        }
 * }
 * 
 * Of course you have to have the typical jhipster-like repositories and service setup
 */
package <%= packageName %>.internal.service;
