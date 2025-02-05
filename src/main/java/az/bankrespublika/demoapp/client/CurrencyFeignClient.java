package az.bankrespublika.demoapp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign отправляет HTTP-запрос
 * API возвращает XML-файл с курсами валют
 * Feign автоматически получает XML как String и передаёт в код
 * name -> name for logging
 * url -> base url for send request
 * value -> continue base url
 * consumes -> response format(XML / JSON)
 */
@FeignClient(name = "currencyClient", url = "https://www.cbar.az")
public interface CurrencyFeignClient {
    @GetMapping(value = "/currencies/{date}.xml", consumes = MediaType.APPLICATION_XML_VALUE)
    String getCurrencies(@PathVariable("date") String date);
}