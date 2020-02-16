package com.frj.auth.lib.dal.ddb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * DynamoDB item representing a user.
 *
 * @author fridge
 */
@DynamoDBTable(tableName = UserDdbItem.TABLE_NAME)
public class UserDdbItem implements DdbItem {

    public static final String TABLE_NAME = "Users";

    public static final String COL_USER_ID = "UserID";
    private static final String COL_PASSWORD = "Password";

    /**
     * Unique user ID.
     */
    @DynamoDBHashKey(attributeName = COL_USER_ID)
    private String userId;

    /**
     * Encrypted password.
     */
    @DynamoDBAttribute(attributeName = COL_PASSWORD)
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}