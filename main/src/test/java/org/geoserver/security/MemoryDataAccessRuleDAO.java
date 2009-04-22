package org.geoserver.security;

import java.util.Properties;

import org.geoserver.catalog.Catalog;

/**
 * A {@link DataAccessRuleDAO} variant that lives in memory
 */
class MemoryDataAccessRuleDAO extends DataAccessRuleDAO {
    
    public MemoryDataAccessRuleDAO(Catalog rawCatalog, Properties props) {
        super(rawCatalog);
        loadRules(props);
    }
    
    @Override
    void checkPropertyFile() {
        // skip checking
        lastModified = Long.MAX_VALUE;
    }
}