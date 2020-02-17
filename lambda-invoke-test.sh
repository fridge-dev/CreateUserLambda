RESPONSE_JSON_FILE=/tmp/invoke_output.txt
aws lambda invoke \
    --function-name CreateUserHandler \
    --cli-binary-format raw-in-base64-out \
    --payload "{
        \"username\": \"$1\",
        \"password\": \"$2\",
        \"spec\": \"SIMPLE/PIN\"
    }" \
    $RESPONSE_JSON_FILE \
&& echo -e "\nResponse paylod:" \
&& cat $RESPONSE_JSON_FILE | jq \
&& rm $RESPONSE_JSON_FILE \
&& echo

