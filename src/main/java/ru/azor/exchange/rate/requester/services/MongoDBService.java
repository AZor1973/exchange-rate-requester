package ru.azor.exchange.rate.requester.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.azor.exchange.rate.requester.dao.RateResponseDao;
import ru.azor.exchange.rate.requester.enums.ErrorValues;
import ru.azor.exchange.rate.requester.models.RateResponse;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MongoDBService {
    private final RateResponseDao dao;

    public void saveRateResponseToMongoDB(RateResponse rateResponse) {
        if (rateResponse == null){
            log.error("Failed saving to MongoDB");
            return;
        }
        Long id = rateResponse.getId();
        if (id == null){
            log.error("Failed saving to MongoDB");
            return;
        }
        if (dao.existsById(id) ||
                rateResponse.getDisclaimer().equals(ErrorValues.NO_SERVICE.name())) {
            log.error("Failed saving to MongoDB");
            return;
        }
        dao.save(rateResponse);
        log.info("Save to MongoDB: " + rateResponse);
    }

    public RateResponse getRateResponseFromMongoDBAsObject(Long id) {
        return dao.findById(id).orElseGet(() ->
                new RateResponse(ErrorValues.NOT_FOUND.name()));
    }

    public RateResponse getLastRateResponse() {
        return dao.findAll(Sort.by(Sort.Direction.DESC, "_id")).get(0);
    }

    public List<Long> getAllTimestamps() {
        List<Long> timestamps = dao.findAll()
                .stream().map(RateResponse::getTimestamp)
                .collect(Collectors.toList());
        log.info("From MongoDB list size: " + timestamps.size());
        return timestamps;
    }

    public boolean deleteRateById(Long id) {
        dao.deleteById(id);
        if (dao.existsById(id)) {
            log.error(id + " FAILED DELETION");
            return false;
        }
        log.info(id + " deleted");
        return true;
    }

//    курс за последние двое суток
    public List<RateResponse> getCurrentRate() {
        return dao.findByTimestampGreaterThan(new Date().getTime()/1000 - 172800);
    }

    public List<RateResponse> getAllRates() {
        return dao.findAll();
    }
}

