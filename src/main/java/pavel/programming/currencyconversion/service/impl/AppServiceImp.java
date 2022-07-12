package pavel.programming.currencyconversion.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pavel.programming.currencyconversion.remotecall.NeoCurrencyConversionInvoker;
import pavel.programming.currencyconversion.remotecall.model.Conversion;
import pavel.programming.currencyconversion.remotecall.model.Currency;
import pavel.programming.currencyconversion.service.AppService;
import pavel.programming.currencyconversion.service.ConversionService;
import pavel.programming.currencyconversion.service.model.ConversionPath;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class AppServiceImp implements AppService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppServiceImp.class);

    private final NeoCurrencyConversionInvoker conversionInvoker;
    private final ConversionService conversionService;

    public AppServiceImp(NeoCurrencyConversionInvoker conversionInvoker, ConversionService conversionService) {
        this.conversionInvoker = conversionInvoker;
        this.conversionService = conversionService;
    }

    @Override
    public void getBestCurrencyConversionFromApiAndSaveToCSV() {
        List<Conversion> currencyConversionList =  conversionInvoker.getCurrencyConversionList();
        LOGGER.debug("API return conversionList: {}",  currencyConversionList);

        Map<Currency, ConversionPath> bestCurrencyConversion = conversionService.getBestCurrencyConversion(currencyConversionList, "CAD", new BigDecimal("100.00"));
        LOGGER.debug("bestCurrencyConversion: {}",  bestCurrencyConversion);
    }
}
