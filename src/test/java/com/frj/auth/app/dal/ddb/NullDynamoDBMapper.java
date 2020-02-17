package com.frj.auth.app.dal.ddb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperTableModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedParallelScanList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.datamodeling.S3ClientCache;
import com.amazonaws.services.dynamodbv2.datamodeling.S3Link;
import com.amazonaws.services.dynamodbv2.datamodeling.ScanResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.s3.model.Region;
import java.util.List;
import java.util.Map;

/**
 * Should probably just use mockito... oh well
 *
 * @author fridge
 */
public class NullDynamoDBMapper implements IDynamoDBMapper {
    @Override
    public <T> DynamoDBMapperTableModel<T> getTableModel(final Class<T> clazz) {
        return null;
    }

    @Override
    public <T> DynamoDBMapperTableModel<T> getTableModel(final Class<T> clazz, final DynamoDBMapperConfig config) {
        return null;
    }

    @Override
    public <T> T load(final Class<T> clazz, final Object hashKey, final DynamoDBMapperConfig config) {
        return null;
    }

    @Override
    public <T> T load(final Class<T> clazz, final Object hashKey) {
        return null;
    }

    @Override
    public <T> T load(final Class<T> clazz, final Object hashKey, final Object rangeKey) {
        return null;
    }

    @Override
    public <T> T load(final T keyObject) {
        return null;
    }

    @Override
    public <T> T load(final T keyObject, final DynamoDBMapperConfig config) {
        return null;
    }

    @Override
    public <T> T load(final Class<T> clazz, final Object hashKey, final Object rangeKey, final DynamoDBMapperConfig config) {
        return null;
    }

    @Override
    public <T> T marshallIntoObject(final Class<T> clazz, final Map<String, AttributeValue> itemAttributes) {
        return null;
    }

    @Override
    public <T> List<T> marshallIntoObjects(final Class<T> clazz, final List<Map<String, AttributeValue>> itemAttributes) {
        return null;
    }

    @Override
    public <T> void save(final T object) {

    }

    @Override
    public <T> void save(final T object, final DynamoDBSaveExpression saveExpression) {

    }

    @Override
    public <T> void save(final T object, final DynamoDBMapperConfig config) {

    }

    @Override
    public <T> void save(final T object, final DynamoDBSaveExpression saveExpression, final DynamoDBMapperConfig config) {

    }

    @Override
    public void delete(final Object object) {

    }

    @Override
    public void delete(final Object object, final DynamoDBDeleteExpression deleteExpression) {

    }

    @Override
    public void delete(final Object object, final DynamoDBMapperConfig config) {

    }

    @Override
    public <T> void delete(final T object, final DynamoDBDeleteExpression deleteExpression, final DynamoDBMapperConfig config) {

    }

    @Override
    public List<DynamoDBMapper.FailedBatch> batchDelete(final Iterable<?> objectsToDelete) {
        return null;
    }

    @Override
    public List<DynamoDBMapper.FailedBatch> batchDelete(final Object... objectsToDelete) {
        return null;
    }

    @Override
    public List<DynamoDBMapper.FailedBatch> batchSave(final Iterable<?> objectsToSave) {
        return null;
    }

    @Override
    public List<DynamoDBMapper.FailedBatch> batchSave(final Object... objectsToSave) {
        return null;
    }

    @Override
    public List<DynamoDBMapper.FailedBatch> batchWrite(final Iterable<?> objectsToWrite, final Iterable<?> objectsToDelete) {
        return null;
    }

    @Override
    public List<DynamoDBMapper.FailedBatch> batchWrite(final Iterable<?> objectsToWrite, final Iterable<?> objectsToDelete, final DynamoDBMapperConfig config) {
        return null;
    }

    @Override
    public Map<String, List<Object>> batchLoad(final Iterable<?> itemsToGet) {
        return null;
    }

    @Override
    public Map<String, List<Object>> batchLoad(final Iterable<?> itemsToGet, final DynamoDBMapperConfig config) {
        return null;
    }

    @Override
    public Map<String, List<Object>> batchLoad(final Map<Class<?>, List<KeyPair>> itemsToGet) {
        return null;
    }

    @Override
    public Map<String, List<Object>> batchLoad(final Map<Class<?>, List<KeyPair>> itemsToGet, final DynamoDBMapperConfig config) {
        return null;
    }

    @Override
    public <T> PaginatedScanList<T> scan(final Class<T> clazz, final DynamoDBScanExpression scanExpression) {
        return null;
    }

    @Override
    public <T> PaginatedScanList<T> scan(final Class<T> clazz, final DynamoDBScanExpression scanExpression, final DynamoDBMapperConfig config) {
        return null;
    }

    @Override
    public <T> PaginatedParallelScanList<T> parallelScan(final Class<T> clazz, final DynamoDBScanExpression scanExpression, final int totalSegments) {
        return null;
    }

    @Override
    public <T> PaginatedParallelScanList<T> parallelScan(final Class<T> clazz, final DynamoDBScanExpression scanExpression, final int totalSegments, final DynamoDBMapperConfig config) {
        return null;
    }

    @Override
    public <T> ScanResultPage<T> scanPage(final Class<T> clazz, final DynamoDBScanExpression scanExpression, final DynamoDBMapperConfig config) {
        return null;
    }

    @Override
    public <T> ScanResultPage<T> scanPage(final Class<T> clazz, final DynamoDBScanExpression scanExpression) {
        return null;
    }

    @Override
    public <T> PaginatedQueryList<T> query(final Class<T> clazz, final DynamoDBQueryExpression<T> queryExpression) {
        return null;
    }

    @Override
    public <T> PaginatedQueryList<T> query(final Class<T> clazz, final DynamoDBQueryExpression<T> queryExpression, final DynamoDBMapperConfig config) {
        return null;
    }

    @Override
    public <T> QueryResultPage<T> queryPage(final Class<T> clazz, final DynamoDBQueryExpression<T> queryExpression) {
        return null;
    }

    @Override
    public <T> QueryResultPage<T> queryPage(final Class<T> clazz, final DynamoDBQueryExpression<T> queryExpression, final DynamoDBMapperConfig config) {
        return null;
    }

    @Override
    public int count(final Class<?> clazz, final DynamoDBScanExpression scanExpression) {
        return 0;
    }

    @Override
    public int count(final Class<?> clazz, final DynamoDBScanExpression scanExpression, final DynamoDBMapperConfig config) {
        return 0;
    }

    @Override
    public <T> int count(final Class<T> clazz, final DynamoDBQueryExpression<T> queryExpression) {
        return 0;
    }

    @Override
    public <T> int count(final Class<T> clazz, final DynamoDBQueryExpression<T> queryExpression, final DynamoDBMapperConfig config) {
        return 0;
    }

    @Override
    public S3ClientCache getS3ClientCache() {
        return null;
    }

    @Override
    public S3Link createS3Link(final String bucketName, final String key) {
        return null;
    }

    @Override
    public S3Link createS3Link(final Region s3region, final String bucketName, final String key) {
        return null;
    }

    @Override
    public S3Link createS3Link(final String s3region, final String bucketName, final String key) {
        return null;
    }

    @Override
    public CreateTableRequest generateCreateTableRequest(final Class<?> clazz) {
        return null;
    }

    @Override
    public DeleteTableRequest generateDeleteTableRequest(final Class<?> clazz) {
        return null;
    }
}
