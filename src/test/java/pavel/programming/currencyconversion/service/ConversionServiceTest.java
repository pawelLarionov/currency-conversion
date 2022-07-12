package pavel.programming.currencyconversion.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pavel.programming.currencyconversion.TestConfiguration;
import pavel.programming.currencyconversion.remotecall.model.Conversion;
import pavel.programming.currencyconversion.remotecall.model.Currency;
import pavel.programming.currencyconversion.service.model.ConversionPath;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Import(TestConfiguration.class)
public class ConversionServiceTest {

    @Autowired
    private ConversionService conversionService;

    @Test
    public void testEmptyConversionList() {
        Map<Currency, ConversionPath> map = conversionService.getBestCurrencyConversion(new ArrayList<>(), "CAD", new BigDecimal("100.00"));
        assertTrue(map.isEmpty());
    }

    /**
     *  The best possible conversion rate should yield the    highest possible amount of currency that you are converting to.
     * Given the following example, the longer route yields the higher amount (96.00) and is therefore the best conversation rate.
     *  Conversions List:
     *  (CAD -> HKD, 0.90)
     *  (CAD -> GBP, 0.80), (GBP -> DOGE, 1), (DOGE -> HKD, 1.2)
     *
     * CAD -> HKD = 90.00
     * CAD -> GBP -> DOGE -> HKD = 96.000
     */
    @Test
    public void testBestFrom() {
        List<Conversion> list = new ArrayList<>();
        list.add(newConversion("0.90", "CAD", "HKD"));

        list.add(newConversion("0.80", "CAD", "GBP"));
        list.add(newConversion("1", "GBP", "DOGE"));
        list.add(newConversion("1.2", "DOGE", "HKD"));

        Map<Currency, ConversionPath> map = conversionService.getBestCurrencyConversion(list, "CAD", new BigDecimal("100.00"));
        assertFalse(map.isEmpty());

        assertEquals(3, map.size());

        // to HKD: CAD -> HKD = 90.000
        // to HKD: CAD -> GBP -> DOGE -> HKD = 96.000  - best
        Currency currency_HCD = list.get(0).getCurrencyTo();
        assertNotNull(map.get(currency_HCD));

        ConversionPath expectedPath =  new ConversionPath(new BigDecimal("96.00"), Arrays.asList(list.get(1), list.get(2), list.get(3)));
        assertEquals(expectedPath, map.get(currency_HCD));

        // to GBP: CAD -> GBP  = 80
        Currency currency_GBP = list.get(1).getCurrencyTo();
        assertNotNull(map.get(currency_GBP));

        ConversionPath expectedPath_GBP =  new ConversionPath(new BigDecimal("80.00"), Collections.singletonList(list.get(1)));
        assertEquals(expectedPath_GBP, map.get(currency_GBP));

        // to DOGE: CAD -> GBP -> DOGE = 80
        Currency currency_DOGE = list.get(2).getCurrencyTo();
        assertNotNull(map.get(currency_DOGE));

        ConversionPath expectedPath_DOGE =  new ConversionPath(new BigDecimal("80.00"), Arrays.asList(list.get(1), list.get(2)));
        assertEquals(expectedPath_DOGE, map.get(currency_DOGE));
    }


    /**
     *  Conversions List:
     *  (CAD -> HKD, 0.90)
     *  (CAD -> GBP, 0.80), (GBP -> DOGE, 1), (DOGE -> HKD, 1.2) (HKD -> GBP, 1)
     */
    @Test
    public void testSkipCycle() {
        List<Conversion> list = new ArrayList<>();
        list.add(newConversion("0.90", "CAD", "HKD"));

        list.add(newConversion("0.80", "CAD", "GBP"));
        list.add(newConversion("1", "GBP", "DOGE"));
        list.add(newConversion("1.2", "DOGE", "HKD"));
        list.add(newConversion("1", "HKD", "GBP"));

        Map<Currency, ConversionPath> map = conversionService.getBestCurrencyConversion(list, "CAD", new BigDecimal("100.00"));
        assertFalse(map.isEmpty());

        assertEquals(3, map.size());

        // to HKD: CAD -> HKD = 90.000
        // to HKD: CAD -> GBP -> DOGE -> HKD = 96.000  - best
        Currency currency_HKD = list.get(0).getCurrencyTo();
        assertNotNull(map.get(currency_HKD));

        ConversionPath expectedPath =  new ConversionPath(new BigDecimal("96.00"), Arrays.asList(list.get(1), list.get(2), list.get(3)));
        assertEquals(expectedPath, map.get(currency_HKD));

        // to GBP: CAD -> GBP -> DOGE = 80
        // to GBP: CAD -> HKD -> GBP = 90 - best
        Currency currency_GBP = list.get(1).getCurrencyTo();
        assertNotNull(map.get(currency_GBP));

        ConversionPath expectedPath_GBP =  new ConversionPath(new BigDecimal("90.00"), Arrays.asList(list.get(0), list.get(4)));
        assertEquals(expectedPath_GBP, map.get(currency_GBP));

        // to DOGE: CAD -> GBP -> DOGE = 80
        // to DOGE: CAD -> HKD -> GBP -> DOGE = 90 - best
        Currency currency_DOGE = list.get(2).getCurrencyTo();
        assertNotNull(map.get(currency_DOGE));

        ConversionPath expectedPath_DOGE =  new ConversionPath(new BigDecimal("90.00"), Arrays.asList(list.get(0), list.get(4), list.get(2)));
        assertEquals(expectedPath_DOGE, map.get(currency_DOGE));
    }

    private Conversion newConversion(String rate, String from, String to) {
        return  new Conversion(new BigDecimal(rate), new Currency(from, from), new Currency(to, to));
    }

}
