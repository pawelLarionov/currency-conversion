package pavel.programming.currencyconversion.remotecall.impl;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pavel.programming.currencyconversion.remotecall.NeoCurrencyConversionInvoker;
import pavel.programming.currencyconversion.remotecall.model.Conversion;
import pavel.programming.currencyconversion.remotecall.model.Currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NeoCurrencyConversionInvokerImp implements NeoCurrencyConversionInvoker {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeoCurrencyConversionInvokerImp.class);

    @Value("${neoFinancial.api.url}")
    private String neoFinancialApiUrl;

    private final RestTemplate neoFinancialRestTemplate;

    public NeoCurrencyConversionInvokerImp(RestTemplate neoFinancialRestTemplate) {
        this.neoFinancialRestTemplate = neoFinancialRestTemplate;
    }

    @Override
    public List<Conversion> getCurrencyConversionList() {
        List<Conversion> result = new ArrayList<>();
        try {
            ResponseEntity<CurrencyConversionResponse[]> response = neoFinancialRestTemplate.getForEntity(neoFinancialApiUrl, CurrencyConversionResponse[].class);
            LOGGER.debug("response: {}", response);

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new RestClientException(String.format("Error on NeoFinancial 'getCurrencyConversionData' call , response: %s", response));
            }
            result = Arrays.stream(response.getBody())
                    .map(r -> new Conversion(
                            r.exchangeRate,
                            new Currency(r.fromCurrencyCode, r.fromCurrencyName),
                            new Currency(r.toCurrencyCode, r.toCurrencyName))).collect(Collectors.toList());

        } catch (Exception e) {
            LOGGER.error("Exception on getCurrencyConversionDataList", e);
        }
        return result;
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static class CurrencyConversionResponse {
        private BigDecimal exchangeRate;
        private String fromCurrencyCode;
        private String fromCurrencyName;
        private String toCurrencyCode;
        private String toCurrencyName;
    }

}
