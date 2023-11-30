package ru.netology.transferservice.core.factory;

import ru.netology.transferservice.contracts.entity.Confirmation;
import ru.netology.transferservice.contracts.factory.ConfirmationFactory;
import ru.netology.transferservice.core.entity.CoreConfirmation;

import java.util.Random;
import java.util.UUID;

public class CoreConfirmationFactory implements ConfirmationFactory {
    private final int size;
    private final String template;
    private final Random random;

    public CoreConfirmationFactory(int size, String template) {
        this.size = size;
        this.template = template;
        this.random = new Random();
    }

    @Override
    public Confirmation create(UUID transactionId) {
        return new CoreConfirmation(generateCode(), transactionId);
    }

    private String generateCode() {
        int i = size;
        StringBuilder builder = new StringBuilder();
        while (--i >= 0) {
            builder.append(template.charAt(random.nextInt(template.length())));
        }

        return builder.toString();
    }
}
