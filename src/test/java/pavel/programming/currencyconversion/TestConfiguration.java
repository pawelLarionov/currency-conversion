package pavel.programming.currencyconversion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pavel.programming.currencyconversion.service.ConversionService;
import pavel.programming.currencyconversion.service.impl.ConversionServiceImpl;

@Configuration
public class TestConfiguration {
    @Bean
    public ConversionService getConversionService() {
        return new ConversionServiceImpl();
    }
}
