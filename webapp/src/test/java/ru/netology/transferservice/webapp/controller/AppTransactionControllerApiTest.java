package ru.netology.transferservice.webapp.controller;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.netology.transferservice.contracts.entity.TransactionStatus;
import ru.netology.transferservice.contracts.input.TransactionConfirmationInput;
import ru.netology.transferservice.contracts.input.TransactionConfirmationResponse;
import ru.netology.transferservice.contracts.input.TransactionCreateInput;
import ru.netology.transferservice.contracts.input.TransactionCreateResponse;
import ru.netology.transferservice.contracts.service.TransactionLogger;
import ru.netology.transferservice.core.input.CoreTransactionConfirmationResponse;
import ru.netology.transferservice.core.input.CoreTransactionCreateResponse;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppTransactionControllerApiTest {

    @LocalServerPort
    private int serverPort;

    @MockBean
    private TransactionCreateInput transactionCreateInput;

    @MockBean
    private TransactionConfirmationInput transactionConfirmationInput;

    @MockBean
    private TransactionLogger transactionLogger;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void createTransfer() {
        URI url = ApiTestHelper.createTestUri("transfer", serverPort);
        UUID transactionId = UUID.randomUUID();
        TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
        TransactionCreateResponse response = new CoreTransactionCreateResponse(transactionId, transactionStatus);
        Mockito.when(transactionCreateInput.create(Mockito.any())).thenReturn(response);
        HttpEntity<String> apiRequest = ApiTestHelper.createApiRequest(appTransactionCreateRequest());

        ResponseEntity<ApiResponse> result = testRestTemplate.postForEntity(url, apiRequest, ApiResponse.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(transactionId, result.getBody().getOperationId());
    }

    @Test
    void confirmOperation() {
        URI url = ApiTestHelper.createTestUri("confirmOperation", serverPort);
        UUID transactionId = UUID.fromString("7aaf79ed-ec87-4b7b-a181-d8ccf0dc101d");
        TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
        TransactionConfirmationResponse response = new CoreTransactionConfirmationResponse(transactionId, transactionStatus);
        Mockito.when(transactionConfirmationInput.confirm(Mockito.any())).thenReturn(response);
        HttpEntity<String> apiRequest = ApiTestHelper.createApiRequest(appTransactionConfirmRequest());

        ResponseEntity<ApiResponse> result = testRestTemplate.postForEntity(url, apiRequest, ApiResponse.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(transactionId, result.getBody().getOperationId());
    }

    private String appTransactionCreateRequest() {
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

    private String appTransactionConfirmRequest() {
        return "{" +
                "  \"operationId\": \"7aaf79ed-ec87-4b7b-a181-d8ccf0dc101d\"," +
                "  \"code\": \"25498\"" +
                "}";
    }

    @Data
    static class ApiResponse {
        private UUID operationId;
    }
}