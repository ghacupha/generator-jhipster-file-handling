package <%= packageName %>.internal.model.sampleDataModel;

import <%= packageName %>.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link CurrencyTable}.
 */
@RestController
@RequestMapping("/api")
public class CurrencyTableResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyTableResource.class);

    private static final String ENTITY_NAME = "depositAnalysisMainCurrencyTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurrencyTableService currencyTableService;

    private final CurrencyTableQueryService currencyTableQueryService;

    public CurrencyTableResource(CurrencyTableService currencyTableService, CurrencyTableQueryService currencyTableQueryService) {
        this.currencyTableService = currencyTableService;
        this.currencyTableQueryService = currencyTableQueryService;
    }

    /**
     * {@code POST  /currency-tables} : Create a new currencyTable.
     *
     * @param currencyTableDTO the currencyTableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new currencyTableDTO, or with status {@code 400 (Bad Request)} if the currencyTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/currency-tables")
    public ResponseEntity<CurrencyTableDTO> createCurrencyTable(@Valid @RequestBody CurrencyTableDTO currencyTableDTO) throws URISyntaxException {
        log.debug("REST request to save CurrencyTable : {}", currencyTableDTO);
        if (currencyTableDTO.getId() != null) {
            throw new BadRequestAlertException("A new currencyTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurrencyTableDTO result = currencyTableService.save(currencyTableDTO);
        return ResponseEntity.created(new URI("/api/currency-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /currency-tables} : Updates an existing currencyTable.
     *
     * @param currencyTableDTO the currencyTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyTableDTO,
     * or with status {@code 400 (Bad Request)} if the currencyTableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the currencyTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/currency-tables")
    public ResponseEntity<CurrencyTableDTO> updateCurrencyTable(@Valid @RequestBody CurrencyTableDTO currencyTableDTO) throws URISyntaxException {
        log.debug("REST request to update CurrencyTable : {}", currencyTableDTO);
        if (currencyTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CurrencyTableDTO result = currencyTableService.save(currencyTableDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, currencyTableDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /currency-tables} : get all the currencyTables.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of currencyTables in body.
     */
    @GetMapping("/currency-tables")
    public ResponseEntity<List<CurrencyTableDTO>> getAllCurrencyTables(CurrencyTableCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CurrencyTables by criteria: {}", criteria);
        Page<CurrencyTableDTO> page = currencyTableQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /currency-tables/count} : count all the currencyTables.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/currency-tables/count")
    public ResponseEntity<Long> countCurrencyTables(CurrencyTableCriteria criteria) {
        log.debug("REST request to count CurrencyTables by criteria: {}", criteria);
        return ResponseEntity.ok().body(currencyTableQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /currency-tables/:id} : get the "id" currencyTable.
     *
     * @param id the id of the currencyTableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currencyTableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/currency-tables/{id}")
    public ResponseEntity<CurrencyTableDTO> getCurrencyTable(@PathVariable Long id) {
        log.debug("REST request to get CurrencyTable : {}", id);
        Optional<CurrencyTableDTO> currencyTableDTO = currencyTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(currencyTableDTO);
    }

    /**
     * {@code DELETE  /currency-tables/:id} : delete the "id" currencyTable.
     *
     * @param id the id of the currencyTableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/currency-tables/{id}")
    public ResponseEntity<Void> deleteCurrencyTable(@PathVariable Long id) {
        log.debug("REST request to delete CurrencyTable : {}", id);

        currencyTableService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/currency-tables?query=:query} : search for the currencyTable corresponding
     * to the query.
     *
     * @param query the query of the currencyTable search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/currency-tables")
    public ResponseEntity<List<CurrencyTableDTO>> searchCurrencyTables(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CurrencyTables for query {}", query);
        Page<CurrencyTableDTO> page = currencyTableService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
