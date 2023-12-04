package ru.netology.transferservice.webapp.input;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.netology.transferservice.contracts.input.TransactionCreateRequest;

@AllArgsConstructor
@Getter
public class AppTransactionCreateRequest implements TransactionCreateRequest {
    @NotEmpty
    private String cardFromNumber;

    @NotEmpty
    private String cardFromValidTill;

    @NotEmpty
    @JsonAlias("cardFromCVV")
    private String cardFromCvv;

    @NotEmpty
    private String cardToNumber;

    @NotNull
    @Valid
    @JsonAlias("amount")
    private Amount amountObject;

    @Override
    public String getCurrency() {
        return amountObject.getCurrency();
    }

    @Override
    public long getAmount() {
        return amountObject.getValue();
    }

    @AllArgsConstructor
    @Getter
    public static class Amount {
        @Positive
        private long value;

        @NotEmpty
        private String currency;
    }
}
