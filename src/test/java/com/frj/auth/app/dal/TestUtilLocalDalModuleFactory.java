package com.frj.auth.app.dal;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.TableStatus;
import com.frj.auth.app.dal.ddb.DdbItem;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * This test utility is responsible for accessing the pkg-private members in the DB module package
 * to expose a public test utility for creating a {@link DataAccessLayerModule} based on a local
 * instance of DynamoDB
 *
 * @author fridge
 */
public class TestUtilLocalDalModuleFactory {

    private final Set<Class<? extends DdbItem>> tablesToCreate;

    @SafeVarargs
    public TestUtilLocalDalModuleFactory(final Class<? extends DdbItem>... tablesToCreate) {
        this.tablesToCreate = new HashSet<>(Arrays.asList(tablesToCreate));
    }

    /**
     * Creates the provided DynamoDB tables in a local DynamoDB instance and returns the corresponding
     * DAL module instance to access your accessors.
     *
     * You can call this in your {@link org.junit.jupiter.api.BeforeEach} method.
     */
    public DataAccessLayerModule create() {
        final AmazonDynamoDB localDb = DynamoDBEmbedded.create().amazonDynamoDB();
        final DynamoDBMapper dynamoDbMapper = DataAccessLayerModule.DynamoDBMapperFactory.create(localDb);
        final DdbTableManager ddbTableManager = new DdbTableManager(localDb, dynamoDbMapper);

        for (Class<? extends DdbItem> table : tablesToCreate) {
            ddbTableManager.createTable(table, 5L, 5L);
        }

        return DataAccessLayerModule.Factory.create(dynamoDbMapper);
    }

    /**
     * This class is responsible for managing table status of various tables in DynamoDB.
     *
     * @author fridge
     */
    private static class DdbTableManager {

        private final AmazonDynamoDB dynamoDb;
        private final IDynamoDBMapper dynamoDbMapper;

        private DdbTableManager(final AmazonDynamoDB dynamoDb, final IDynamoDBMapper dynamoDbMapper) {
            this.dynamoDb = Objects.requireNonNull(dynamoDb);
            this.dynamoDbMapper = Objects.requireNonNull(dynamoDbMapper);
        }

        /**
         * @throws IllegalStateException if the table for the class doesn't exist.
         */
        public <T extends DdbItem> void assertTableExists(final Class<T> clazz) {
            String tableName = dynamoDbMapper.generateCreateTableRequest(clazz).getTableName();
            if (!doesTableExist(tableName)) {
                throw new IllegalStateException(String.format("Expected DynamoDB table %s to exist.", tableName));
            }
        }

        private boolean doesTableExist(final String tableName) {
            try {
                String tableStatus = dynamoDb.describeTable(tableName)
                        .getTable()
                        .getTableStatus();

                return TableStatus.ACTIVE.toString().equals(tableStatus);
            } catch (ResourceNotFoundException e) {
                return false;
            }
        }

        /**
         * Creates a new table in DynamoDB.
         */
        public <T extends DdbItem> CreateTableResult createTable(final Class<T> clazz, final long rcu, final long wcu) {
            CreateTableRequest tableRequest = dynamoDbMapper.generateCreateTableRequest(clazz).withProvisionedThroughput(new ProvisionedThroughput(rcu, wcu));
            setGsiCapacityAndProjection(tableRequest.getGlobalSecondaryIndexes(), rcu, wcu);
            setLsiProjection(tableRequest.getLocalSecondaryIndexes());

            String tableName = tableRequest.getTableName();
            if (doesTableExist(tableName)) {
                throw new IllegalStateException(String.format("About to create table %s in DynamoDB, but it already exists.", tableName));
            }

            return dynamoDb.createTable(tableRequest);
        }

        private static void setGsiCapacityAndProjection(final List<GlobalSecondaryIndex> globalSecondaryIndexes, final long rcu, final long wcu) {
            if (globalSecondaryIndexes == null) {
                return;
            }

            for (GlobalSecondaryIndex globalSecondaryIndex : globalSecondaryIndexes) {
                globalSecondaryIndex.setProvisionedThroughput(new ProvisionedThroughput(rcu, wcu));
                globalSecondaryIndex.setProjection(new Projection().withProjectionType(ProjectionType.ALL));
            }
        }

        private static void setLsiProjection(final List<LocalSecondaryIndex> localSecondaryIndexes) {
            if (localSecondaryIndexes == null) {
                return;
            }

            for (LocalSecondaryIndex localSecondaryIndex : localSecondaryIndexes) {
                localSecondaryIndex.setProjection(new Projection().withProjectionType(ProjectionType.ALL));
            }
        }
    }
}