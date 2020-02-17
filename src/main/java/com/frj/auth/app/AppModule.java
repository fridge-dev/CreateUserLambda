package com.frj.auth.app;

import com.frj.auth.app.dal.DataAccessLayerModule;
import com.frj.auth.app.password.PasswordHasher;
import java.util.Objects;

/**
 * The class responsible for instantiating objects and wiring them together.
 *
 * Aka Dependency Injection. But I don't want to use Spring/Guice because I want minimal dependencies.
 *
 * @author fridge
 */
public final class AppModule {

    /*
     * ========== Static Singleton Code ===========
     */

    /**
     * {@link AppModule} is a singleton-by-choice. We instantiate/expose a singleton, however, mainly for
     * testing purposes, you're allowed to create your own instance.
     */
    private static final AppModule INSTANCE = Factory.create();
    public static AppModule getInstance() {
        return INSTANCE;
    }

    /* VisibleForTests */ static class Factory {

        private static AppModule create() {
            return create(DataAccessLayerModule.Factory.create());
        }

        /* VisibleForTests */ static AppModule create(final DataAccessLayerModule dalModule) {
            return new AppModule(
                    new CreateUserHandler(dalModule.getUserLoginDataAccessor(), PasswordHasher.Factory.create())
            );
        }
    }

    /*
     * ========== Instance members ===========
     */

    private final CreateUserHandler createUserHandler;

    private AppModule(final CreateUserHandler createUserHandler) {
        this.createUserHandler = Objects.requireNonNull(createUserHandler);
    }

    public CreateUserHandler getCreateUserHandler() {
        return createUserHandler;
    }
}
