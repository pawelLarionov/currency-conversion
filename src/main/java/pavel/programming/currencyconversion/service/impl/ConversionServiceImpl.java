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
            Map<String, List<Conversion>> conversionGraphMap = currencyConversionList.stream()
                    .collect(Collectors.groupingBy(m -> m.getCurrencyFrom().getCode()));
            LOGGER.debug("conversionGraphMap: {}", conversionGraphMap);

            for (Conversion conversion : conversionGraphMap.get(startCurrencyCode)) {
                getPath(conversionGraphMap, bestCurrencyConversionMap, new ConversionPath(startAmount), conversion);
            }
        }

        LOGGER.debug("bestCurrencyConversionMap: {}",  bestCurrencyConversionMap);
        return bestCurrencyConversionMap;
    }

    private void getPath(Map<String, List<Conversion>> conversionGraphMap, Map<Currency, ConversionPath> resultMap, ConversionPath prevPath, Conversion conversion) {
        ConversionPath conversionPath = new ConversionPath(prevPath.getAmount(), prevPath.getPath())
                .addConversionAndMultiplyAmount(conversion);


        Currency currencyTo = conversion.getCurrencyTo();
        replaceInMapIfBetterConversion(resultMap, currencyTo, conversionPath);

        List<Conversion> conversionData = conversionGraphMap.get(currencyTo.getCode());
        if (conversionData == null) {
            return;
        }

        for (Conversion nextConversion : conversionData) {
            getPath(conversionGraphMap, resultMap,  conversionPath,  nextConversion);
        }
    }

    private void replaceInMapIfBetterConversion(Map<Currency, ConversionPath> resultMap, Currency currency, ConversionPath conversionPath) {
        ConversionPath pathInMap = resultMap.get(currency);
        if (pathInMap == null) {
            resultMap.put(currency, conversionPath);
        } else if (pathInMap.getAmount().compareTo(conversionPath.getAmount()) < 0) {
            resultMap.replace(currency, conversionPath);
        }
    }

}
