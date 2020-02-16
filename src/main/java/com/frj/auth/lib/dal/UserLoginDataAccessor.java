package com.frj.auth.lib.dal;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.frj.auth.lib.dal.ddb.DdbExceptionTranslator;
import com.frj.auth.lib.dal.ddb.DdbExpressionFactory;
import com.frj.auth.lib.dal.ddb.DynamoDbAccessor;
import com.frj.auth.lib.dal.ddb.UserDdbItem;
import java.util.Objects;
import java.util.Optional;

/**
 * Database accessor abstraction for User data.
 *
 * NOTE: This is the layer where we our vocabulary maps username <-> userId
 *
 * @author fridge
 */
public class UserLoginDataAccessor implements DataAccessor<UserLoginDataKey, UserLoginData> {

    private static final String CREATE_USER_CONDITION_FAILED_MESSAGE = "Failed to create user because username is already taken.";

    private static final DynamoDBSaveExpression SAVE_CONDITION = DdbExpressionFactory.newSaveExpressionItemDoesntExist(UserDdbItem.COL_USER_ID);

    private final DynamoDbAccessor<UserDdbItem> ddbAccessor;

    public static UserLoginDataAccessor getAccessor(final IDynamoDBMapper dynamoDbMapper) {
        return new UserLoginDataAccessor(new DynamoDbAccessor<>(dynamoDbMapper, UserDdbItem.class));
    }

    private UserLoginDataAccessor(final DynamoDbAccessor<UserDdbItem> ddbAccessor) {
        this.ddbAccessor = Objects.requireNonNull(ddbAccessor);
    }

    /**
     * Create new unique user using username as the user ID.
     */
    @Override
    public void create(final UserLoginData userLoginData) {
        UserDdbItem item = domainTypeToItem(userLoginData);


        DdbExceptionTranslator.conditionalWrite(
                () -> ddbAccessor.saveItem(item, SAVE_CONDITION),
                CREATE_USER_CONDITION_FAILED_MESSAGE
        );
    }

    private UserDdbItem domainTypeToItem(final UserLoginData userLoginData) {
        UserDdbItem item = new UserDdbItem();

        item.setUserId(userLoginData.getUsername());
        item.setPassword(userLoginData.getPassword());

        return item;
    }

    /**
     * Load user by the unique username (i.e. user ID).
     */
    @Override
    public Optional<UserLoginData> load(final UserLoginDataKey key) {
        return ddbAccessor.loadItem(key.getUsername())
                .map(this::itemToDomainType);
    }

    private UserLoginData itemToDomainType(final UserDdbItem item) {
        return new UserLoginData(item.getUserId(), item.getPassword());
    }
}