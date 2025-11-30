package cz.cvut.omo.sem.scm.seafood.resource;

import cz.cvut.omo.sem.scm.seafood.type.resource.Currency;
import lombok.Value;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Value
public class Money {
    BigDecimal amount;
    Currency currency;

    private Money(Number rawAmount, Currency currency) {
        this.amount = BigDecimal.valueOf(rawAmount.doubleValue())
                .setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }

    // double — для 1200.50
    public static Money czk(double amount) { return new Money(amount, Currency.CZK); }
    public static Money eur(double amount) { return new Money(amount, Currency.EUR); }
    public static Money usd(double amount) { return new Money(amount, Currency.USD); }

    // long — для 50000, 1000000 — без потери точности
    public static Money czk(long amount)   { return new Money(amount, Currency.CZK); }
    public static Money eur(long amount)   { return new Money(amount, Currency.EUR); }
    public static Money usd(long amount)   { return new Money(amount, Currency.USD); }

    @Override
    public String toString() {
        return "%s %s".formatted(amount, currency.getSymbol());
    }
}