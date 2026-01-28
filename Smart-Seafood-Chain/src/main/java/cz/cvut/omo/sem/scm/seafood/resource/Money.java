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
        // Converting Number to BigDecimal with scaling to 2 decimal places
        this.amount = BigDecimal.valueOf(rawAmount.doubleValue())
                .setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }

    // double — for values like 1200.50
    public static Money czk(double amount) { return new Money(amount, Currency.CZK); }
    public static Money eur(double amount) { return new Money(amount, Currency.EUR); }
    public static Money usd(double amount) { return new Money(amount, Currency.USD); }

    // long — for large integers like 50000, 1000000 — ensuring precision
    public static Money czk(long amount)   { return new Money(amount, Currency.CZK); }
    public static Money eur(long amount)   { return new Money(amount, Currency.EUR); }
    public static Money usd(long amount)   { return new Money(amount, Currency.USD); }

    /**
     * Subtracts the specified amount from this Money object.
     * Returns a new instance (Immutable).
     */
    public Money subtract(Money other) {
        checkCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    /**
     * Adds the specified amount to this Money object.
     * Returns a new instance (Immutable).
     */
    public Money add(Money other) {
        checkCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    private void checkCurrency(Money other) {
        if (this.currency != other.currency) {
            throw new IllegalArgumentException("Currency mismatch: Cannot operate on %s and %s"
                    .formatted(this.currency, other.currency));
        }
    }

    @Override
    public String toString() {
        return "%s %s".formatted(amount, currency.getSymbol());
    }
}