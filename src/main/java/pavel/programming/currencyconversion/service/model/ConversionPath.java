package pavel.programming.currencyconversion.service.model;

import pavel.programming.currencyconversion.remotecall.model.Conversion;
import pavel.programming.currencyconversion.remotecall.model.Currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Information about Path conversion of currency
 */
public class ConversionPath {
    /**
     * Path conversion of currency
     */
    private final List<Conversion> path = new ArrayList<>();
    private final Set<Currency> currencyFromInPath = new HashSet<>();

    /**
     * Final amount after conversion by this path
     */
    private BigDecimal amount;

    public ConversionPath(BigDecimal amount) {
        this.amount = amount;
    }

    public ConversionPath(BigDecimal amount, List<Conversion> path) {
        this.amount = amount;
        this.path.addAll(path);
        for (Conversion conversion : path) {
            currencyFromInPath.add(conversion.getCurrencyFrom());
        }
    }

    public ConversionPath addConversionAndMultiplyAmount(Conversion conversion) {
        this.path.add(conversion);
        currencyFromInPath.add(conversion.getCurrencyFrom());
        this.amount = this.amount.multiply(conversion.getExchangeRate());
        return this;
    }

    public List<Conversion> getPath() {
        return path;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean willBeLoopIfAdd(Conversion conversion) {
        return currencyFromInPath.contains(conversion.getCurrencyTo());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversionPath that = (ConversionPath) o;
        return Objects.equals(path, that.path) && amount != null && amount.compareTo(that.amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, amount);
    }

    @Override
    public String toString() {
        return "ConversionPath{" +
                "path=" + path +
                ", amount=" + amount +
                '}';
    }
}
