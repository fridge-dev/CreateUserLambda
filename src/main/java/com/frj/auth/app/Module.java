package com.frj.auth.app;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.frj.auth.app.dal.UserLoginDataAccessor;

/**
 * The class responsible for instantiating objects and wiring them together.
 *
 * Aka Dependency Injection. But I don't want to use Spring/Guice because I want minimal dependencies.
 *
 * @author fridge
 */
public final class Module {

    private static final DynamoDBMapper DYNAMO_DB_SINGLETON = initializeDynamoDb();

    private final CreateUserHandler USER_CREATOR = makeUserCreator();

    public CreateUserHandler getUserCreator() {
        return USER_CREATOR;
    }

    private CreateUserHandler makeUserCreator() {
        final UserLoginDataAccessor userLoginDataAccessor = UserLoginDataAccessor.getAccessor(DYNAMO_DB_SINGLETON);

        return new CreateUserHandler(userLoginDataAccessor);
    }

    private static DynamoDBMapper initializeDynamoDb() {
        final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClient.builder()
                .withRegion(Regions.US_WEST_2)
                .build();

        final DynamoDBMapperConfig dynamoDBMapperConfig = DynamoDBMapperConfig.builder()
                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.EVENTUAL)
                .build();

        return new DynamoDBMapper(amazonDynamoDB, dynamoDBMapperConfig);
    }

}
