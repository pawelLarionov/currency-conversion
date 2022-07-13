package pavel.programming.currencyconversion.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pavel.programming.currencyconversion.remotecall.model.Currency;
import pavel.programming.currencyconversion.service.CsvService;
import pavel.programming.currencyconversion.service.model.ConversionPath;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CsvServiceImpl implements CsvService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsvServiceImpl.class);

    private static final CSVFormat CSV_FORMAT= CSVFormat.DEFAULT.builder()
            .setHeader("Currency Code","Country","Amount of currency","Path for the best conversion rate")
            .build();

    @Value("${outputCsvFile.name}")
    private String outputCsvFileName;

    @Override
    public Path createCSVFile(Map<Currency, ConversionPath> currencyConversionMap)  {
        Path paths = Paths.get(outputCsvFileName);
        try (
             BufferedWriter writer = Files.newBufferedWriter(paths);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSV_FORMAT)
        ) {

            for (Map.Entry<Currency, ConversionPath> entry : currencyConversionMap.entrySet()) {
                Currency currency = entry.getKey();
                ConversionPath conversionPath = entry.getValue();
                csvPrinter.printRecord(
                        currency.getCode(),
                        currency.getName(),
                        conversionPath.getAmount(),
                        conversionPath.getPath().stream().map(
                                c -> c.getCurrencyFrom().getCode()).collect(Collectors.joining(" | ")
                        )
                );
                csvPrinter.flush();
            }

        } catch (Exception exception) {
            LOGGER.error("Exception on createCSVFile", exception);
            throw new RuntimeException(exception);
        }
        return paths;
    }
}
