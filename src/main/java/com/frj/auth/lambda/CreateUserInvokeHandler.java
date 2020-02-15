package com.frj.auth.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.frj.auth.lib.CreateUserReply;
import com.frj.auth.lib.CreateUserRequest;
import com.frj.auth.lib.Module;
import com.frj.auth.lib.UserCreator;

/**
 * My first handler. *tear*
 *
 * @author fridge
 */
public class CreateUserInvokeHandler implements RequestHandler<CreateUserInvoke, CreateUserInvokeReply> {

    private static final UserCreator USER_CREATOR = new Module().getUserCreator();

    public CreateUserInvokeReply handleRequest(final CreateUserInvoke input, final Context context) {
        final CreateUserRequest createUserRequest = convertRequest(input);
        final CreateUserReply createUserReply = USER_CREATOR.createUser(createUserRequest);
        return convertReply(createUserReply);
    }

    private CreateUserInvokeReply convertReply(final CreateUserReply createUserReply) {
        final CreateUserInvokeReply reply = new CreateUserInvokeReply();
        reply.setFailureMessage(createUserReply.getFailureMessage());
        return reply;
    }

    private CreateUserRequest convertRequest(final CreateUserInvoke input) {
        final String[] split = input.getUserSpec().split("/");
        return new CreateUserRequest(
                input.getUsername(),
                input.getPassword(),
                CreateUserRequest.UsernameSpec.valueOf(split[0]),
                CreateUserRequest.PasswordSpec.valueOf(split[1])
        );
    }

}
