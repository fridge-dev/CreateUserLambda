package com.frj.auth.app.dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.frj.auth.app.dal.ddb.UserLoginCredsDdbItem;
import com.frj.auth.app.dal.models.ConditionalWriteException;
import com.frj.auth.app.dal.models.UserLoginData;
import com.frj.auth.app.dal.models.UserLoginDataKey;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserLoginDataAccessorTest {

    private static final String USERNAME = "fridge";
    private static final String PASSWORD = "H^g97R%vk,";

    private static final TestUtilLocalDalModuleFactory localDalModuleFactory = new TestUtilLocalDalModuleFactory(UserLoginCredsDdbItem.class);

    private UserLoginDataAccessor accessor;

    @BeforeEach
    void setup() {
        accessor = localDalModuleFactory.create().getUserLoginDataAccessor();
    }

    @Test
    void create() throws Exception {
        UserLoginData user = newUserLoginData();

        accessor.create(user);
    }

    @Test
    void create_SameUsername() throws Exception {
        UserLoginData user = newUserLoginData();

        accessor.create(user);

        assertThrows(ConditionalWriteException.class, () -> accessor.create(user));
    }

    @Test
    void load() throws Exception {
        UserLoginData savedUser = newUserLoginData();
        UserLoginDataKey userKey = new UserLoginDataKey(savedUser.getUsername());

        // 1. Load
        assertFalse(accessor.load(userKey).isPresent());

        // 2. Create
        accessor.create(savedUser);

        // 3. Load (again)
        Optional<UserLoginData> loadedUser = accessor.load(userKey);

        assertEquals(savedUser, loadedUser.orElse(null));
    }

    private UserLoginData newUserLoginData() {
        return new UserLoginData(
                USERNAME,
                PASSWORD
        );
    }
}