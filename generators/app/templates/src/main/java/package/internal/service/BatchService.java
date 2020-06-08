package io.github.deposits.app.service;

import io.github.deposits.service.dto.DepositAccountDTO;

import java.util.List;

/**
 * This interface is intended to be implemented transactionally but with batch items persisted for every commit
 */
public interface BatchService<T> {

    /**
     * Save an entity.
     *
     * @param entities entity to save.
     * @return the persisted entity.
     */
    List<T> save(List<T> entities);

    /**
     * The above call only persists entities to the relations db repository
     * for efficiency sake.
     * Therefore to have it all in an index one needs to call this function
     * @param entities
     * @return
     */
    void index(List<T> entities);
}
