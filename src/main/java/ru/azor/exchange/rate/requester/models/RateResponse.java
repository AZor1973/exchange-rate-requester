package ru.azor.exchange.rate.requester.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "rates")
public class RateResponse {
    @Id
    private Long id;
    private String disclaimer;
    private String license;
    private Long timestamp;
    private String base;
    private Rates rates;

    public RateResponse(String disclaimer){
        this.disclaimer = disclaimer;
    }
}
