package ru.azor.exchange.rate.requester.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.azor.exchange.rate.requester.models.RateResponse;
import java.util.List;

public interface RateResponseDao extends MongoRepository<RateResponse, Long> {

    List<RateResponse> findByTimestampGreaterThan(Long timestamp);
}
