package cz.cvut.omo.sem.scm.seafood.type.resource;

public enum Currency {
    CZK("Kč"),
    EUR("€"),
    USD("$"),
    NOK("kr");

    private final String symbol;

    Currency(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}