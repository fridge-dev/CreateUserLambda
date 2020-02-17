package com.frj.auth.app;

import com.frj.auth.app.dal.TestUtilLocalDalModuleFactory;
import com.frj.auth.app.dal.ddb.DdbItem;

/**
 * Test utility for creating an {@link AppModule} instance with DynamoDB local.
 *
 * @author fridge
 */
public class TestUtilLocalAppModuleFactory {

    private final TestUtilLocalDalModuleFactory testUtilLocalDalModuleFactory;

    private TestUtilLocalAppModuleFactory(final TestUtilLocalDalModuleFactory testUtilLocalDalModuleFactory) {
        this.testUtilLocalDalModuleFactory = testUtilLocalDalModuleFactory;
    }

    @SafeVarargs
    public TestUtilLocalAppModuleFactory(final Class<? extends DdbItem>... tablesToCreate) {
        this(new TestUtilLocalDalModuleFactory(tablesToCreate));
    }

    public AppModule create() {
        return AppModule.Factory.create(testUtilLocalDalModuleFactory.create());
    }
}