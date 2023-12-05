package ru.netology.transferservice.webapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.transferservice.contracts.entity.TransactionStatusCode;
import ru.netology.transferservice.webapp.factory.AppTransactionControllerRequestFactory;
import ru.netology.transferservice.webapp.model.AppResponse;
import ru.netology.transferservice.webapp.service.AppTransactionCsvLogger.Item;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AppTransactionControllerIntegrationTest {
    private static final int CONTAINER_PORT = 8088;
    private static final Path LOG_FILE = Path.of("/app/logs/transactions-log-integration-test.csv");
    private static final String HOST_LOG = "../logs";
    private static UUID savedTransactionId;
    @Container
    private static GenericContainer<?> serviceApp = new GenericContainer<>(ApiTestHelper.DOCKER_TAG)
            .withEnv("SERVER_PORT", String.valueOf(CONTAINER_PORT))
            .withEnv("TRANSFERSERVICE_TRANSACTION_CONFIRMATION_TEMPLATE", "11111111")
            .withEnv("TRANSFERSERVICE_TRANSACTION_CONFIRMATION_SIZE", "3")
            .withEnv("TRANSFERSERVICE_TRANSACTION_LOG_FILENAME", LOG_FILE.toString())
            .withExposedPorts(CONTAINER_PORT)
            .withFileSystemBind(HOST_LOG, "/app/logs", BindMode.READ_WRITE);
    private final TestRestTemplate testRestTemplate = new TestRestTemplate();
    private final AppTransactionControllerRequestFactory requestFactory = new AppTransactionControllerRequestFactory();

    @AfterAll
    public static void tearDownClass() {
        try {
            Path logHostFile = Path.of(HOST_LOG, LOG_FILE.getFileName().toString());
            Files.delete(logHostFile);
            log.info("Delete test transactions log file: " + logHostFile);
        } catch (IOException iex) {
            log.error(iex.toString());
        }
    }

    @Test
    @Order(1)
    void whenTransferCreateValid_thenSuccess() throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(Item.Fields.class)
                .setSkipHeaderRecord(true)
                .build();
        Path logHostFile = Path.of(HOST_LOG, LOG_FILE.getFileName().toString());
        URI url = ApiTestHelper.createTestUri("transfer", serviceApp.getMappedPort(CONTAINER_PORT));
        HttpEntity<String> apiRequest = ApiTestHelper.createApiRequest(requestFactory.appTransactionCreateRequest());

        ResponseEntity<AppResponse> result = testRestTemplate.postForEntity(url, apiRequest, AppResponse.class);

        log.info(result.toString());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertNotNull(result.getBody().getOperationId());
        UUID transactionId = result.getBody().getOperationId();
        try (Reader in = new FileReader(logHostFile.toFile())) {
            List<CSVRecord> logItems = csvFormat.parse(in).getRecords();
            assertEquals(1, logItems.size());
            CSVRecord resultItem = logItems.get(0);
            assertNotNull(resultItem.get(Item.Fields.loggedAt));
            assertEquals(transactionId.toString(), resultItem.get(Item.Fields.transactionId));
            assertEquals(TransactionStatusCode.NEW.code, resultItem.get(Item.Fields.status));
            assertEquals("1001", resultItem.get(Item.Fields.amount));
            assertEquals("10", resultItem.get(Item.Fields.commission));
            assertEquals("1234123412341234", resultItem.get(Item.Fields.cardFromNumber));
            assertEquals("1234123412341234", resultItem.get(Item.Fields.cardToNumber));
        }
        savedTransactionId = transactionId;
    }

    @Test
    @Order(2)
    void whenConfirmOperationValid_thenSuccess() throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(Item.Fields.class)
                .setSkipHeaderRecord(true)
                .build();
        Path logHostFile = Path.of(HOST_LOG, LOG_FILE.getFileName().toString());
        URI url = ApiTestHelper.createTestUri("confirmOperation", serviceApp.getMappedPort(CONTAINER_PORT));
        HttpEntity<String> apiRequest = ApiTestHelper.createApiRequest(
                requestFactory.appTransactionConfirmRequest(savedTransactionId.toString()));

        ResponseEntity<AppResponse> result = testRestTemplate.postForEntity(url, apiRequest, AppResponse.class);

        log.info(result.toString());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(savedTransactionId, result.getBody().getOperationId());
        try (Reader in = new FileReader(logHostFile.toFile())) {
            List<CSVRecord> logItems = csvFormat.parse(in).getRecords();
            assertEquals(2, logItems.size());
            CSVRecord resultItem = logItems.get(1);
            assertNotNull(resultItem.get(Item.Fields.loggedAt));
            assertEquals(savedTransactionId.toString(), resultItem.get(Item.Fields.transactionId));
            assertEquals(TransactionStatusCode.CONFIRMED.code, resultItem.get(Item.Fields.status));
            assertEquals("1001", resultItem.get(Item.Fields.amount));
            assertEquals("10", resultItem.get(Item.Fields.commission));
            assertEquals("1234123412341234", resultItem.get(Item.Fields.cardFromNumber));
            assertEquals("1234123412341234", resultItem.get(Item.Fields.cardToNumber));
        }
    }
}
