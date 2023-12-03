package ru.netology.transferservice.webapp.input;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
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
    @Getter(AccessLevel.NONE)
    private Amount amount;

    @Override
    public String getCurrency() {
        return amount.getCurrency();
    }

    @Override
    public long getAmount() {
        return amount.getValue();
    }

    @AllArgsConstructor
    @Getter
    public static class Amount {
        @NotEmpty
        @Min(0)
        private long value;

        @NotEmpty
        private String currency;
    }
}
