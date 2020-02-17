package com.frj.auth.app.dal;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import java.util.Objects;

/**
 * The module responsible for instantiating Data Access Layer (DAL) classes.
 *
 * @author fridge
 */
public final class DataAccessLayerModule {

    /*
     * ========== Static Singleton Code ===========
     */

    /**
     * This class is responsible for creating instances of {@link DataAccessLayerModule}.
     */
     public static class Factory {
        public static DataAccessLayerModule create() {
            return create(DynamoDBMapperFactory.create());
        }

        /* VisibleForTests */ static DataAccessLayerModule create(final DynamoDBMapper dynamoDBMapper) {
            return new DataAccessLayerModule(
                    UserLoginDataAccessor.getAccessor(dynamoDBMapper)
            );
        }
    }

    /**
     * This class is responsible for configuring DynamoDB client and creating {@link DynamoDBMapper} instances.
     */
    static final class DynamoDBMapperFactory {

        /**
         * For production use.
         */
        static DynamoDBMapper create() {
            final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClient.builder()
                    .withRegion(Regions.US_WEST_2)
                    .build();

            return create(amazonDynamoDB);
        }

        /**
         * For unit tests.
         */
        static DynamoDBMapper create(final AmazonDynamoDB amazonDynamoDB) {
            final DynamoDBMapperConfig dynamoDBMapperConfig = DynamoDBMapperConfig.builder()
                    .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.EVENTUAL)
                    .build();

            return new DynamoDBMapper(amazonDynamoDB, dynamoDBMapperConfig);
        }
    }

    /*
     * ========== Instance members ===========
     */

    private final UserLoginDataAccessor userLoginDataAccessor;

    private DataAccessLayerModule(final UserLoginDataAccessor userLoginDataAccessor) {
        this.userLoginDataAccessor = Objects.requireNonNull(userLoginDataAccessor);
    }

    public UserLoginDataAccessor getUserLoginDataAccessor() {
        return userLoginDataAccessor;
    }
}
