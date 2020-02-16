package com.frj.auth.lib.dal;

import java.util.Optional;

/**
 * Generic accessor for Data Access Layer (DAL).
 *
 * Less generic access patterns (batch/paginated reads, updates) should not go here, and instead,
 * in the specific extension of this interface.
 *
 * @author fridge
 */
public interface DataAccessor<K extends AppDataKey, V extends AppDataModel> {

    /**
     * Create a new entry in the database.
     */
    void create(V data) throws DataAccessLayerException;

    /**
     * Loads the unique entry from the database.
     */
    Optional<V> load(K key) throws DataAccessLayerException;

}