package pavel.programming.currencyconversion.service;

/**
 * Main application Service - connects all services
 */
public interface AppService {

    /**
     *  Get conversion list from the API
     *  Calculate optimal currency conversion paths
     *  Generate and save CSV file that contains optimal conversions paths
     */
    void getBestCurrencyConversionFromApiAndSaveToCSV();
}
