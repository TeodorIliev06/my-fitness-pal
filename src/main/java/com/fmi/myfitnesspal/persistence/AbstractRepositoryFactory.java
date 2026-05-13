package com.fmi.myfitnesspal.persistence;

import java.util.function.Supplier;

public abstract class AbstractRepositoryFactory {

    protected final boolean persistToFile;
    protected final PersistenceStoreFactory storeFactory;

    protected AbstractRepositoryFactory(boolean persistToFile, PersistenceStoreFactory storeFactory) {
        this.persistToFile = persistToFile;
        this.storeFactory = storeFactory;
    }

    protected final <DOMAIN, REPO extends DOMAIN> DOMAIN resolveRepository(
            Supplier<DOMAIN> inMemorySupplier,
            Supplier<REPO> persistentSupplier) {
        if (!persistToFile) {
            return inMemorySupplier.get();
        }

        return persistentSupplier.get();
    }
}
