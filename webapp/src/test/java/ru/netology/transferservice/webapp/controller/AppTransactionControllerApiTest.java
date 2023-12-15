package ru.netology.transferservice.webapp.controller;

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
import ru.netology.transferservice.contracts.exception.TransactionException;
import ru.netology.transferservice.contracts.exception.TransactionExceptionCode;
import ru.netology.transferservice.contracts.input.TransactionConfirmationInput;
import ru.netology.transferservice.contracts.input.TransactionConfirmationResponse;
import ru.netology.transferservice.contracts.input.TransactionCreateInput;
import ru.netology.transferservice.contracts.input.TransactionCreateResponse;
import ru.netology.transferservice.contracts.service.TransactionLogger;
import ru.netology.transferservice.core.input.CoreTransactionConfirmationResponse;
import ru.netology.transferservice.core.input.CoreTransactionCreateResponse;
import ru.netology.transferservice.webapp.factory.AppTransactionControllerRequestFactory;
import ru.netology.transferservice.webapp.model.AppError;
import ru.netology.transferservice.webapp.model.AppResponse;

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

    @Autowired
    private AppTransactionControllerRequestFactory requestFactory;

    @Test
    void whenTransferCreateValid_thenSuccess() {
        URI url = ApiTestHelper.createTestUri("transfer", serverPort);
        UUID transactionId = UUID.randomUUID();
        TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
        TransactionCreateResponse response = new CoreTransactionCreateResponse(transactionId, transactionStatus);
        Mockito.when(transactionCreateInput.create(Mockito.any())).thenReturn(response);
        HttpEntity<String> apiRequest = ApiTestHelper.createApiRequest(requestFactory.appTransactionCreateRequest());

        ResponseEntity<AppResponse> result = testRestTemplate.postForEntity(url, apiRequest, AppResponse.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(transactionId, result.getBody().getOperationId());
    }

    @Test
    void whenTransferRequestMalformed_thenBadRequestWithError() {
        URI url = ApiTestHelper.createTestUri("transfer", serverPort);
        HttpEntity<String> apiRequest = ApiTestHelper.createApiRequest(
                requestFactory.appTransactionCreateErrorRequest());

        ResponseEntity<AppError> result = testRestTemplate.postForEntity(url, apiRequest, AppError.class);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().getId());
        assertNotNull(result.getBody().getMessage());
    }

    @Test
    void whenTransferRequestProducesTransactionException_thenBadRequestWithError() {
        URI url = ApiTestHelper.createTestUri("transfer", serverPort);
        HttpEntity<String> apiRequest = ApiTestHelper.createApiRequest(
                requestFactory.appTransactionCreateCardNotValidRequest());
        Mockito.when(transactionCreateInput.create(Mockito.any()))
                .thenThrow(new TransactionException(TransactionExceptionCode.CARD_FROM_NOT_FOUND));

        ResponseEntity<AppError> result = testRestTemplate.postForEntity(url, apiRequest, AppError.class);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(TransactionExceptionCode.CARD_FROM_NOT_FOUND.getId(), result.getBody().getId());
        assertEquals(TransactionExceptionCode.CARD_FROM_NOT_FOUND.getMessage(), result.getBody().getMessage());
    }

    @Test
    void whenConfirmOperationValid_thenSuccess() {
        URI url = ApiTestHelper.createTestUri("confirmOperation", serverPort);
        UUID transactionId = UUID.fromString("7aaf79ed-ec87-4b7b-a181-d8ccf0dc101d");
        TransactionStatus transactionStatus = Mockito.mock(TransactionStatus.class);
        TransactionConfirmationResponse response = new CoreTransactionConfirmationResponse(transactionId,
                transactionStatus);
        Mockito.when(transactionConfirmationInput.confirm(Mockito.any())).thenReturn(response);
        HttpEntity<String> apiRequest = ApiTestHelper.createApiRequest(requestFactory.appTransactionConfirmRequest());

        ResponseEntity<AppResponse> result = testRestTemplate.postForEntity(url, apiRequest, AppResponse.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(transactionId, result.getBody().getOperationId());
    }

    @Test
    void whenConfirmRequestMalformed_thenBadRequestWithError() {
        URI url = ApiTestHelper.createTestUri("confirmOperation", serverPort);
        HttpEntity<String> apiRequest = ApiTestHelper.createApiRequest(
                requestFactory.appTransactionConfirmErrorRequest());

        ResponseEntity<AppError> result = testRestTemplate.postForEntity(url, apiRequest, AppError.class);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().getId());
        assertNotNull(result.getBody().getMessage());
    }

    @Test
    void whenConfirmRequestProducesTransactionException_thenBadRequestWithError() {
        URI url = ApiTestHelper.createTestUri("confirmOperation", serverPort);
        HttpEntity<String> apiRequest = ApiTestHelper.createApiRequest(requestFactory.appTransactionConfirmRequest());
        Mockito.when(transactionConfirmationInput.confirm(Mockito.any()))
                .thenThrow(new TransactionException(TransactionExceptionCode.TRANSACTION_NOT_FOUND));

        ResponseEntity<AppError> result = testRestTemplate.postForEntity(url, apiRequest, AppError.class);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(TransactionExceptionCode.TRANSACTION_NOT_FOUND.getId(), result.getBody().getId());
        assertEquals(TransactionExceptionCode.TRANSACTION_NOT_FOUND.getMessage(), result.getBody().getMessage());
    }

    @Test
    void whenConfirmRequestUUIDMalformed_thenBadRequestWithError() {
        URI url = ApiTestHelper.createTestUri("confirmOperation", serverPort);
        HttpEntity<String> apiRequest = ApiTestHelper.createApiRequest(
                requestFactory.appTransactionConfirmRequest("123"));

        ResponseEntity<AppError> result = testRestTemplate.postForEntity(url, apiRequest, AppError.class);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().getId());
        assertNotNull(result.getBody().getMessage());
    }
}