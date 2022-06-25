package ru.azor.exchange.rate.requester.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.azor.exchange.rate.requester.models.RateResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {
    @Value("${base-currency}")
    private String USD;
    private final WebClient currencyWebClient;
    private final MongoDBService mongoDBService;
    @Value("${params.app-id}")
    private String appId;

    @Scheduled(cron = "${interval-in-cron}")
    public void getExchangeRate() {
        try {
            String response = currencyWebClient
                    .get()
                    .uri("?app_id=" + appId + "&base=" + USD)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            ObjectMapper mapper = new ObjectMapper();
            RateResponse rate = mapper.readValue(response, RateResponse.class);
            rate.setId(rate.getTimestamp());
            mongoDBService.saveRateResponseToMongoDB(rate);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
