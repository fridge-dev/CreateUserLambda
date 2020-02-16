package com.frj.auth.lib;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.frj.auth.lib.dal.UserLoginDataAccessor;

/**
 * TODO
 *
 * @author TODO
 */
public final class Module {

    private static final DynamoDBMapper DYNAMO_DB_SINGLETON = initializeDynamoDb();

    private final UserCreator USER_CREATOR = makeUserCreator();

    public UserCreator getUserCreator() {
        return USER_CREATOR;
    }

    private UserCreator makeUserCreator() {
        final UserLoginDataAccessor userLoginDataAccessor = UserLoginDataAccessor.getAccessor(DYNAMO_DB_SINGLETON);

        return new UserCreator(userLoginDataAccessor);
    }

    private static DynamoDBMapper initializeDynamoDb() {
        final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClient.builder()
                .withRegion(Regions.US_WEST_2)
                .withCredentials(InstanceProfileCredentialsProvider.getInstance())
                .build();

        final DynamoDBMapperConfig dynamoDBMapperConfig = DynamoDBMapperConfig.builder()
                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.EVENTUAL)
                .build();

        return new DynamoDBMapper(amazonDynamoDB, dynamoDBMapperConfig);
    }

}
