package com.frj.auth.app.dal;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.frj.auth.app.dal.ddb.DdbExceptionTranslator;
import com.frj.auth.app.dal.ddb.DdbExpressionFactory;
import com.frj.auth.app.dal.ddb.DynamoDbAccessor;
import com.frj.auth.app.dal.ddb.UserLoginCredsDdbItem;
import com.frj.auth.app.dal.models.DataAccessor;
import com.frj.auth.app.dal.models.UserLoginData;
import com.frj.auth.app.dal.models.UserLoginDataKey;
import java.util.Objects;
import java.util.Optional;

/**
 * Database accessor abstraction for User data.
 *
 * NOTE: This is the layer where we our vocabulary maps username <-> userId
 *
 * @author fridge
 */
/* PRIVATE */ final class UserLoginDataAccessor implements DataAccessor<UserLoginDataKey, UserLoginData> {

    private static final String CREATE_USER_CONDITION_FAILED_MESSAGE = "Failed to create user because username is already taken.";

    private static final DynamoDBSaveExpression SAVE_CONDITION = DdbExpressionFactory.newSaveExpressionItemDoesntExist(UserLoginCredsDdbItem.COL_USERNAME);

    private final DynamoDbAccessor<UserLoginCredsDdbItem> ddbAccessor;

    public static UserLoginDataAccessor getAccessor(final IDynamoDBMapper dynamoDbMapper) {
        return new UserLoginDataAccessor(new DynamoDbAccessor<>(dynamoDbMapper, UserLoginCredsDdbItem.class));
    }

    private UserLoginDataAccessor(final DynamoDbAccessor<UserLoginCredsDdbItem> ddbAccessor) {
        this.ddbAccessor = Objects.requireNonNull(ddbAccessor);
    }

    /**
     * Create new unique user using username as the user ID.
     */
    @Override
    public void create(final UserLoginData userLoginData) {
        UserLoginCredsDdbItem item = domainTypeToItem(userLoginData);

        DdbExceptionTranslator.conditionalWrite(
                () -> ddbAccessor.saveItem(item, SAVE_CONDITION),
                CREATE_USER_CONDITION_FAILED_MESSAGE
        );
    }

    private UserLoginCredsDdbItem domainTypeToItem(final UserLoginData userLoginData) {
        UserLoginCredsDdbItem item = new UserLoginCredsDdbItem();

        item.setUsername(userLoginData.getUsername());
        item.setPassword(userLoginData.getPassword());

        return item;
    }

    /**
     * Load user by the unique username.
     */
    @Override
    public Optional<UserLoginData> load(final UserLoginDataKey key) {
        return ddbAccessor.loadItem(key.getUsername())
                .map(this::itemToDomainType);
    }

    private UserLoginData itemToDomainType(final UserLoginCredsDdbItem item) {
        return new UserLoginData(item.getUsername(), item.getPassword());
    }
}