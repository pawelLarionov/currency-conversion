package pavel.programming.currencyconversion.remotecall.model;

import java.math.BigDecimal;

/**
 * Data about conversion rate form one currency to another
 */
public class Conversion {
    /**
     * Exchange rate of currency
     */
    private final BigDecimal exchangeRate;

    /*
    * The currency from which the conversion will be made
    */
    private final Currency currencyFrom;

    /*
     * The currency to which the conversion will be made
     */
    private final Currency currencyTo;

    public Conversion(BigDecimal exchangeRate, Currency currencyFrom, Currency currencyTo) {
        this.exchangeRate = exchangeRate;
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public Currency getCurrencyFrom() {
        return currencyFrom;
    }

    public Currency getCurrencyTo() {
        return currencyTo;
    }

    @Override
    public String toString() {
        return "Conversion{rate=" + exchangeRate +", from=" + currencyFrom +", to=" + currencyTo +'}';
    }
}
