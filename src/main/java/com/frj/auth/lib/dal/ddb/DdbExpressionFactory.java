package com.frj.auth.lib.dal.ddb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains utilities related to building {@link DynamoDBSaveExpression} or {@link DynamoDBDeleteExpression} objects.
 *
 * @author fridge
 */
public class DdbExpressionFactory {

    /**
     * Make a DynamoDB save expression with the condition that the provided column doesn't exist.
     *
     * Call with your table's hash key as the columnName and this conditional will be for the item not existing.
     */
    public static DynamoDBSaveExpression newSaveExpressionItemDoesntExist(final String columnName) {
        Map<String, ExpectedAttributeValue> expectUserDoesntExist = new HashMap<>();
        expectUserDoesntExist.put(columnName, new ExpectedAttributeValue(false));

        return new DynamoDBSaveExpression().withExpected(expectUserDoesntExist);
    }

    /**
     * Make a DynamoDB Condition that could be used for filtering in a scan or query. Use with your column's name.
     *
     * Example: {@link com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression#withQueryFilterEntry(String, Condition)}
     */
    public static Condition newConditionColumnEquals(final String columnValue) {
        AttributeValue attributeValue = new AttributeValue().withS(columnValue);

        return new Condition()
                .withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(attributeValue);
    }
}
