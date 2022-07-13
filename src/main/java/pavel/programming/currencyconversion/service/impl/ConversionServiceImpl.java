package pavel.programming.currencyconversion.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pavel.programming.currencyconversion.Application;
import pavel.programming.currencyconversion.remotecall.NeoCurrencyConversionInvoker;
import pavel.programming.currencyconversion.remotecall.model.Conversion;
import pavel.programming.currencyconversion.remotecall.model.Currency;
import pavel.programming.currencyconversion.service.ConversionService;
import pavel.programming.currencyconversion.service.model.ConversionPath;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConversionServiceImpl implements ConversionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConversionServiceImpl.class);

    @Override
    public Map<Currency, ConversionPath> getBestCurrencyConversion(List<Conversion> currencyConversionList, String startCurrencyCode, BigDecimal startAmount) {
        Map<Currency, ConversionPath> bestCurrencyConversionMap = new HashMap<>();

        if (!currencyConversionList.isEmpty()) {
            /*
             * Collect all Conversion objects with currencyFrom().getCode() as the key.
             * For example, if there were conversions:  "List<{from=CAD, to=HKD},{from=CAD, to=USD},{from=USD, to=HKD},{from=CAD, to=BRL}>"
             * Then I get:
             * Key1 CAD: List <{from=CAD, to=HKD}, {from=CAD, to=USD}, {from=CAD, to=BRL}>
             * Key2 USD: List <{from=USD, to=HKD} >
             */
            Map<String, List<Conversion>> conversionGraphMap = currencyConversionList.stream()
                    .collect(Collectors.groupingBy(m -> m.getCurrencyFrom().getCode()));
            LOGGER.debug("conversionGraphMap: {}", conversionGraphMap);

            // I pull out from the map by the key "CAD" since this list contains all the next Conversions that we can get from CAD .
            for (Conversion conversion : conversionGraphMap.get(startCurrencyCode)) {
                // for each this conversion run recursive method getPath
                getPath(conversionGraphMap, bestCurrencyConversionMap, new ConversionPath(startAmount), conversion);
            }
        }

        LOGGER.debug("bestCurrencyConversionMap: {}",  bestCurrencyConversionMap);
        return bestCurrencyConversionMap;
    }

    private void getPath(Map<String, List<Conversion>> conversionGraphMap, Map<Currency, ConversionPath> resultMap, ConversionPath prevPath, Conversion conversion) {
        if (prevPath.willBeLoopIfAdd(conversion)) {
            return; // loop detected, return from recursion
        }

        ConversionPath conversionPath = new ConversionPath(prevPath.getAmount(), prevPath.getPath())  // Clone prevPAth
                .addConversionAndMultiplyAmount(conversion); // add conversion to this path and calculate amount with this conversion

        Currency currencyTo = conversion.getCurrencyTo(); // The name of the currency the conversion path ends with
        replaceInMapIfBetterConversion(resultMap, currencyTo, conversionPath); // Replace in result map if it is better conversion path

        // pull out from the conversionGraphMap by the key {currency code on the end of path} since this list contains all the next Conversions that we can get
        List<Conversion> conversionData = conversionGraphMap.get(currencyTo.getCode());
        if (conversionData == null) {
            return;  // If null we don't have any another conversions after this, return from recursion
        }

        for (Conversion nextConversion : conversionData) {
            // for each this conversion run recursive method getPath
            getPath(conversionGraphMap, resultMap,  conversionPath,  nextConversion);
        }
    }

    /**
     * Check if we dont have in result map 'conversion path' ending in currency {currency} - we simply add it in map
     * else (if we already have 'conversion path' ending in currency {currency})
     *    - check if amount in 'new path' better than amount in 'current path' - than replace 'current path' with the 'new path'
     */
    private void replaceInMapIfBetterConversion(Map<Currency, ConversionPath> resultMap, Currency currency, ConversionPath newConversionPath) {
        ConversionPath currentConversionPath = resultMap.get(currency);
        if (currentConversionPath == null) {
            resultMap.put(currency, newConversionPath);
        } else if (currentConversionPath.getAmount().compareTo(newConversionPath.getAmount()) < 0) {
            resultMap.replace(currency, newConversionPath);
        }
    }

}
