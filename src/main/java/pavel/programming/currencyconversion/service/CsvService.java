package pavel.programming.currencyconversion.service;

import pavel.programming.currencyconversion.remotecall.model.Currency;
import pavel.programming.currencyconversion.service.model.ConversionPath;

import java.nio.file.Path;
import java.util.Map;

public interface CsvService {

    /**
     * Generate a CSV file as an output that contains optimal conversions paths.
     * The file have the following format:
     * Currency Code (ie. AUD, USD, BTC)
     * Country (ie. Australia, USA, Bitcoin)
     * Amount of currency
     * Path for the best conversion rate, pipe delimited (ie. CAD | GBP | EUR)
     *
     * @param currencyConversionMap  Map with optimal conversions paths
     */
    Path createCSVFile(Map<Currency, ConversionPath> currencyConversionMap);
}
