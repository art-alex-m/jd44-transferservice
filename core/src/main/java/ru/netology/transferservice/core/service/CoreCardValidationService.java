package ru.netology.transferservice.core.service;

import ru.netology.transferservice.contracts.entity.Card;
import ru.netology.transferservice.contracts.service.CardUserInput;
import ru.netology.transferservice.contracts.service.CardValidationService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class CoreCardValidationService implements CardValidationService {
    private final String datePattern = "MM/yy";
    private final long startDate = LocalDate.of(2000, 1, 31).toEpochDay();

    @Override
    public boolean isValid(Card card, CardUserInput userInput) {
        if (card.getCvv().equals(userInput.getCvv())) {
            if (card.getValidTill().equals(userInput.getValidTill())) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
                dateFormat.setLenient(false);
                dateFormat.set2DigitYearStart(new Date(startDate));
                try {
                    return dateFormat.parse(userInput.getValidTill()).after(new Date());
                } catch (Exception ignore) {
                }
            }
        }

        return false;
    }
}
