package pavel.programming.currencyconversion.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pavel.programming.currencyconversion.remotecall.NeoCurrencyConversionInvoker;
import pavel.programming.currencyconversion.remotecall.model.Conversion;
import pavel.programming.currencyconversion.remotecall.model.Currency;
import pavel.programming.currencyconversion.service.AppService;
import pavel.programming.currencyconversion.service.ConversionService;
import pavel.programming.currencyconversion.service.CsvService;
import pavel.programming.currencyconversion.service.model.ConversionPath;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Service
public class AppServiceImp implements AppService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppServiceImp.class);

    private final NeoCurrencyConversionInvoker conversionInvoker;
    private final ConversionService conversionService;
    private final CsvService csvService;

    @Value("${startCurrencyCode:CAD}")
    private String startCurrencyCode;

    @Value("${startAmount:100.00}")
    private String startAmount;

    public AppServiceImp(NeoCurrencyConversionInvoker conversionInvoker, ConversionService conversionService, CsvService csvService) {
        this.conversionInvoker = conversionInvoker;
        this.conversionService = conversionService;
        this.csvService = csvService;
    }

    @Override
    public void getBestCurrencyConversionFromApiAndSaveToCSV() {
        try {
            List<Conversion> currencyConversionList =  conversionInvoker.getCurrencyConversionList();
            if (!currencyConversionList.isEmpty()) {
                LOGGER.info("API return conversionList: {}",  currencyConversionList);

                Map<Currency, ConversionPath> bestCurrencyConversion = conversionService.getBestCurrencyConversion(currencyConversionList, startCurrencyCode, new BigDecimal(startAmount));
                LOGGER.info("bestCurrencyConversion: {}", bestCurrencyConversion);

                Path path = csvService.createCSVFile(bestCurrencyConversion);
                LOGGER.info("File created successfully {}", path.toAbsolutePath());
            } else {
                LOGGER.info("API return empty conversionList");
            }

        } catch (Exception ex) {
            LOGGER.error("Something went wrong", ex);
        }
    }
}
