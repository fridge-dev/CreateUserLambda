package com.frj.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * My first handler. *tear*
 *
 * @author fridge
 */
public class CreateUserHandler implements RequestHandler<CreateUserRequest, CreateUserReply> {

    public CreateUserReply handleRequest(final CreateUserRequest input, final Context context) {
        return new CreateUserReply(String.format(
                "You want me to create user %s with pw %s and spec %s? Frick no!",
                input.getUsername(),
                input.getPassword(),
                input.getUserSpec()
        ));
    }

}
