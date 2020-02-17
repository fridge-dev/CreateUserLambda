#!/bin/sh

mvn clean install
aws lambda update-function-code \
    --function-name CreateUserHandler \
    --zip-file fileb://./target/CreateUserLambda-1.0-SNAPSHOT.jar

