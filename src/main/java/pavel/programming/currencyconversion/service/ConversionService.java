package pavel.programming.currencyconversion.service;

import pavel.programming.currencyconversion.remotecall.model.Conversion;
import pavel.programming.currencyconversion.remotecall.model.Currency;
import pavel.programming.currencyconversion.service.model.ConversionPath;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *  Service for currency conversion
 */
public interface ConversionService {

    /**
     * Get list for best conversion paths from currency {startCurrencyCode} to every currency from the {conversionList}
     * assuming we start with {startAmount}.
     *
     * (The best possible conversion rate should yield the highest possible amount of currency that we are converting to.)
     *
     * @param currencyConversionList List of currency conversions
     * @param startCurrencyCode currency code for which we will look for the best conversion
     * @param startAmount amount for which we will look for the best conversion
     *
     * @return Map <Currency, ConversionPath>  List of best conversion path from {startCurrencyCode} to Currency
     */
    Map<Currency, ConversionPath> getBestCurrencyConversion(List<Conversion> currencyConversionList, String startCurrencyCode, BigDecimal startAmount);
}
