package pavel.programming.currencyconversion.remotecall;

import pavel.programming.currencyconversion.remotecall.model.Conversion;

import java.util.List;

/**
 * Invoker NeoFinancial currency conversion API
 */
public interface NeoCurrencyConversionInvoker {

    /**
     * @return List of all currency conversion data
     */
    List<Conversion> getCurrencyConversionList();
}
