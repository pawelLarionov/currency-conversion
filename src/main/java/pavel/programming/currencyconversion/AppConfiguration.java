package pavel.programming.currencyconversion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = {"pavel.programming.currencyconversion"})
public class AppConfiguration {

    @Value("${neoFinancial.api.httClient.connectTimeout:5000}")
    private int neoFinancialHttClientConnectTimeout;

    @Bean
    public RestTemplate getNeoFinancialRestTemplate() {
        return new RestTemplate(getClientHttpRequestFactory());
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(neoFinancialHttClientConnectTimeout);
        return clientHttpRequestFactory;
    }
}
