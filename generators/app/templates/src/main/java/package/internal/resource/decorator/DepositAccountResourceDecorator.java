package io.github.deposits.app.resource.decorator;

import io.github.deposits.service.dto.DepositAccountCriteria;
import io.github.deposits.service.dto.DepositAccountDTO;
import io.github.deposits.web.rest.DepositAccountResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.List;

@Component("depositAccountResourceDecorator")
public class DepositAccountResourceDecorator implements IDepositAccountResource {

    private final DepositAccountResource depositAccountResource;

    public DepositAccountResourceDecorator(final DepositAccountResource depositAccountResource) {
        this.depositAccountResource = depositAccountResource;
    }

    /**
     * {@code POST  /deposit-accounts} : Create a new depositAccount.
     *
     * @param depositAccountDTO the depositAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depositAccountDTO, or with status {@code 400 (Bad Request)} if the depositAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    public ResponseEntity<DepositAccountDTO> createDepositAccount(final DepositAccountDTO depositAccountDTO) throws URISyntaxException {
        return depositAccountResource.createDepositAccount(depositAccountDTO);
    }

    /**
     * {@code PUT  /deposit-accounts} : Updates an existing depositAccount.
     *
     * @param depositAccountDTO the depositAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depositAccountDTO, or with status {@code 400 (Bad Request)} if the depositAccountDTO is not valid, or
     * with status {@code 500 (Internal Server Error)} if the depositAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Override
    public ResponseEntity<DepositAccountDTO> updateDepositAccount(final DepositAccountDTO depositAccountDTO) throws URISyntaxException {
        return depositAccountResource.updateDepositAccount(depositAccountDTO);
    }

    /**
     * {@code GET  /deposit-accounts} : get all the depositAccounts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depositAccounts in body.
     */
    @Override
    public ResponseEntity<List<DepositAccountDTO>> getAllDepositAccounts(final DepositAccountCriteria criteria, final Pageable pageable) {
        return depositAccountResource.getAllDepositAccounts(criteria, pageable);
    }

    /**
     * {@code GET  /deposit-accounts/count} : count all the depositAccounts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @Override
    public ResponseEntity<Long> countDepositAccounts(final DepositAccountCriteria criteria) {
        return depositAccountResource.countDepositAccounts(criteria);
    }

    /**
     * {@code GET  /deposit-accounts/:id} : get the "id" depositAccount.
     *
     * @param id the id of the depositAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depositAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @Override
    public ResponseEntity<DepositAccountDTO> getDepositAccount(final Long id) {
        return depositAccountResource.getDepositAccount(id);
    }

    /**
     * {@code DELETE  /deposit-accounts/:id} : delete the "id" depositAccount.
     *
     * @param id the id of the depositAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Override
    public ResponseEntity<Void> deleteDepositAccount(final Long id) {
        return depositAccountResource.deleteDepositAccount(id);
    }

    /**
     * {@code SEARCH  /_search/deposit-accounts?query=:query} : search for the depositAccount corresponding to the query.
     *
     * @param query    the query of the depositAccount search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @Override
    public ResponseEntity<List<DepositAccountDTO>> searchDepositAccounts(final String query, final Pageable pageable) {
        return depositAccountResource.searchDepositAccounts(query, pageable);
    }
}
