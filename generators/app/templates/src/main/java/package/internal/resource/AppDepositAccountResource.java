package io.github.deposits.app.resource;

import io.github.deposits.app.resource.decorator.IDepositAccountResource;
import io.github.deposits.service.DepositAccountQueryService;
import io.github.deposits.service.dto.DepositAccountCriteria;
import io.github.deposits.service.dto.DepositAccountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing {@link io.github.deposits.domain.DepositAccount}.
 */
@Slf4j
@RestController
@RequestMapping("/api/app")
public class AppDepositAccountResource implements IDepositAccountResource, ReportList<DepositAccountDTO, DepositAccountCriteria> {

    private final DepositAccountQueryService depositAccountQueryService;
    private final IDepositAccountResource depositAccountResourceDecorator;

    public AppDepositAccountResource(final DepositAccountQueryService depositAccountQueryService, final IDepositAccountResource depositAccountResourceDecorator) {
        this.depositAccountQueryService = depositAccountQueryService;
        this.depositAccountResourceDecorator = depositAccountResourceDecorator;
    }

    /**
     * {@code POST  /deposit-accounts} : Create a new depositAccount.
     *
     * @param depositAccountDTO the depositAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depositAccountDTO, or with status {@code 400 (Bad Request)} if the depositAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deposit-accounts")
    public ResponseEntity<DepositAccountDTO> createDepositAccount(@RequestBody DepositAccountDTO depositAccountDTO) throws URISyntaxException {
        return depositAccountResourceDecorator.createDepositAccount(depositAccountDTO);
    }

    @GetMapping("/list/deposit-accounts")
    public ResponseEntity<List<DepositAccountDTO>> getEntityList(@RequestBody DepositAccountCriteria criteria) {
        // Find for blank criteria
        log.info("Request to fetch the entire list of deposits. Hold fast to something....");
        return ResponseEntity.ok(depositAccountQueryService.findByCriteria(new DepositAccountCriteria()));
    }

    /**
     * {@code PUT  /deposit-accounts} : Updates an existing depositAccount.
     *
     * @param depositAccountDTO the depositAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depositAccountDTO, or with status {@code 400 (Bad Request)} if the depositAccountDTO is not valid, or
     * with status {@code 500 (Internal Server Error)} if the depositAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deposit-accounts")
    public ResponseEntity<DepositAccountDTO> updateDepositAccount(@RequestBody DepositAccountDTO depositAccountDTO) throws URISyntaxException {
        return depositAccountResourceDecorator.updateDepositAccount(depositAccountDTO);
    }

    /**
     * {@code GET  /deposit-accounts} : get all the depositAccounts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depositAccounts in body.
     */
    @GetMapping("/deposit-accounts")
    public ResponseEntity<List<DepositAccountDTO>> getAllDepositAccounts(DepositAccountCriteria criteria, Pageable pageable) {
        return depositAccountResourceDecorator.getAllDepositAccounts(criteria, pageable);
    }

    /**
     * {@code GET  /deposit-accounts/count} : count all the depositAccounts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/deposit-accounts/count")
    public ResponseEntity<Long> countDepositAccounts(DepositAccountCriteria criteria) {
        return depositAccountResourceDecorator.countDepositAccounts(criteria);
    }

    /**
     * {@code GET  /deposit-accounts/:id} : get the "id" depositAccount.
     *
     * @param id the id of the depositAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depositAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deposit-accounts/{id}")
    public ResponseEntity<DepositAccountDTO> getDepositAccount(@PathVariable Long id) {
        return depositAccountResourceDecorator.getDepositAccount(id);
    }

    /**
     * {@code DELETE  /deposit-accounts/:id} : delete the "id" depositAccount.
     *
     * @param id the id of the depositAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deposit-accounts/{id}")
    public ResponseEntity<Void> deleteDepositAccount(@PathVariable Long id) {
        return depositAccountResourceDecorator.deleteDepositAccount(id);
    }

    /**
     * {@code SEARCH  /_search/deposit-accounts?query=:query} : search for the depositAccount corresponding to the query.
     *
     * @param query    the query of the depositAccount search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/deposit-accounts")
    public ResponseEntity<List<DepositAccountDTO>> searchDepositAccounts(@RequestParam String query, Pageable pageable) {
        return depositAccountResourceDecorator.searchDepositAccounts(query, pageable);
    }
}
