package pavel.programming.currencyconversion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pavel.programming.currencyconversion.remotecall.NeoCurrencyConversionInvoker;
import pavel.programming.currencyconversion.remotecall.model.Conversion;

import java.util.List;

@SpringBootApplication
public class Application  implements CommandLineRunner {
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	@Autowired
	private NeoCurrencyConversionInvoker conversionInvoker;

	public static void main(String[] args) {
		LOGGER.info("------------------------------------------------------------------------------------------------");
		LOGGER.info("Currency conversion application STARTED");
		LOGGER.info("------------------------------------------------------------------------------------------------");
		SpringApplication.run(Application.class, args);
		LOGGER.info("------------------------------------------------------------------------------------------------");
		LOGGER.info("Currency conversion application FINISHED");
		LOGGER.info("------------------------------------------------------------------------------------------------");
	}

	@Override
	public void run(String... args) {
		LOGGER.info("GET getCurrencyConversionDataList by API");
		List<Conversion> currencyConversionList =  conversionInvoker.getCurrencyConversionList();

		if (currencyConversionList.isEmpty()) {
			LOGGER.error("Error conversionData is empty");
			return;
		}

		LOGGER.info("ConversionDataList: {}",  currencyConversionList.toString());
	}
}
