package com.frj.auth.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.frj.auth.app.AppModule;
import com.frj.auth.app.CreateUserHandler;
import com.frj.auth.app.CreateUserReply;
import com.frj.auth.app.CreateUserRequest;
import java.util.concurrent.TimeUnit;

/**
 * My first handler. *tear*
 *
 * @author fridge
 */
public class CreateUserLambdaHandler implements RequestHandler<CreateUserLambdaRequest, CreateUserLambdaReply> {

    private static final CreateUserHandler USER_CREATOR = AppModule.getInstance().getCreateUserHandler();

    public CreateUserLambdaReply handleRequest(final CreateUserLambdaRequest invoke, final Context context) {
        final long start = System.nanoTime();
        try {
            return doHandle(invoke);
        } finally {
            final long duration = System.nanoTime() - start;
            System.out.println(String.format("Timing - CreateUserLambdaHandler#handleRequest: %sms", TimeUnit.NANOSECONDS.toMillis(duration)));
        }
    }

    private CreateUserLambdaReply doHandle(final CreateUserLambdaRequest invoke) {
        final CreateUserRequest createUserRequest = convertRequest(invoke);
        final CreateUserReply createUserReply = USER_CREATOR.createUser(createUserRequest);
        return convertReply(createUserReply);
    }

    private CreateUserRequest convertRequest(final CreateUserLambdaRequest lambdaRequest) {
        final String[] split = required(lambdaRequest.getSpec(), "Spec").split("/");
        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid spec format. Requires exactly one separator char.");
        }

        final CreateUserRequest.UsernameSpec usernameSpec = getValidEnum(CreateUserRequest.UsernameSpec.class, split[0]);
        final CreateUserRequest.PasswordSpec passwordSpec = getValidEnum(CreateUserRequest.PasswordSpec.class, split[1]);

        return new CreateUserRequest(
                required(lambdaRequest.getUsername(), "Username"),
                required(lambdaRequest.getPassword(), "Password"),
                usernameSpec,
                passwordSpec
        );
    }

    private CreateUserLambdaReply convertReply(final CreateUserReply createUserReply) {
        final CreateUserLambdaReply reply = new CreateUserLambdaReply();
        reply.setFailureMessage(createUserReply.getFailureMessage());
        return reply;
    }

    private static String required(final String requiredField, final String fieldName) {
        if (requiredField == null || requiredField.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return requiredField;
    }

    private static <E extends Enum<E>> E getValidEnum(final Class<E> clazz, final String string) {
        try {
            return Enum.valueOf(clazz, string);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("Unsupported %s '%s'", clazz.getSimpleName(), string));
        }
    }
}
