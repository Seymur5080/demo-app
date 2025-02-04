package az.bankrespublika.demoapp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currencyClient", url = "https://www.cbar.az")
public interface CurrencyFeignClient {
    @GetMapping(value = "/currencies/{date}.xml", consumes = MediaType.APPLICATION_XML_VALUE)
    String getCurrencies(@PathVariable("date") String date);
}