package com.frj.auth.app.dal.ddb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * DynamoDB item representing a user's login credentials.
 *
 * @author fridge
 */
@DynamoDBTable(tableName = UserLoginCredsDdbItem.TABLE_NAME)
public class UserLoginCredsDdbItem implements DdbItem {

    public static final String TABLE_NAME = "UserLogin";

    public static final String COL_USERNAME = "Username";
    private static final String COL_PASSWORD = "Password";

    /**
     * Unique username.
     */
    @DynamoDBHashKey(attributeName = COL_USERNAME)
    private String username;

    /**
     * Encrypted password.
     */
    @DynamoDBAttribute(attributeName = COL_PASSWORD)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}