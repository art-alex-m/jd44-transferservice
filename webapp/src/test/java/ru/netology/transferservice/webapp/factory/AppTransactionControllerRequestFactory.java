package ru.netology.transferservice.webapp.factory;

import org.springframework.stereotype.Component;

@Component
public class AppTransactionControllerRequestFactory {
    public String appTransactionCreateRequest() {
        return "{" +
                "  \"cardFromNumber\": \"1234123412341234\"," +
                "  \"cardFromValidTill\": \"09/55\"," +
                "  \"cardFromCVV\": \"123\"," +
                "  \"cardToNumber\": \"1234123412341234\"," +
                "  \"amount\": {" +
                "    \"value\": 1001," +
                "    \"currency\": \"RUB\"" +
                "  }" +
                "}";
    }

    public String appTransactionConfirmRequest() {
        return appTransactionConfirmRequest("7aaf79ed-ec87-4b7b-a181-d8ccf0dc101d");
    }

    public String appTransactionConfirmRequest(String transactionId) {
        return "{" +
                "  \"operationId\": \"" + transactionId + "\"," +
                "  \"code\": \"111\"" +
                "}";
    }

    public String appTransactionConfirmErrorRequest() {
        return "{" +
                "  \"operationId\": \"7aaf79ed-ec87-4b7b-a181-d8ccf0dc101d\"," +
                "  \"code\": \"\"" +
                "}";
    }

    public String appTransactionCreateErrorRequest() {
        return "{" +
                "  \"cardFromNumber\": \"\"," +
                "  \"cardFromValidTill\": \"09/55\"," +
                "  \"cardFromCVV\": \"123\"," +
                "  \"cardToNumber\": \"1234123412341234\"," +
                "  \"amount\": {" +
                "    \"value\": 1001," +
                "    \"currency\": \"RUB\"" +
                "  }" +
                "}";
    }

    public String appTransactionCreateCardNotValidRequest() {
        return "{" +
                "  \"cardFromNumber\": \"4321432143214321\"," +
                "  \"cardFromValidTill\": \"09/55\"," +
                "  \"cardFromCVV\": \"123\"," +
                "  \"cardToNumber\": \"1234123412341234\"," +
                "  \"amount\": {" +
                "    \"value\": 1001," +
                "    \"currency\": \"RUB\"" +
                "  }" +
                "}";
    }
}
