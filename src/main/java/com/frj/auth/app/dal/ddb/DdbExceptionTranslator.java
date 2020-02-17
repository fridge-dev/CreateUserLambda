package com.frj.auth.app.dal.ddb;

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.frj.auth.app.dal.models.ConditionalWriteException;

/**
 * This class is responsible for translating DynamoDB SDK exceptions to our custom DAL layer exceptions.
 *
 * @author fridge
 */
public class DdbExceptionTranslator {

    /**
     * Wraps any DDB write using a conditional check.
     */
    public static void conditionalWrite(final Runnable runnable, final String exceptionMessage) {
        try {
            runnable.run();
        } catch (ConditionalCheckFailedException e) {
            throw new ConditionalWriteException(exceptionMessage, e);
        }
    }

}