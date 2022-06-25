package ru.azor.exchange.rate.requester.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.azor.exchange.rate.requester.services.MongoDBService;

@RestController
@RequiredArgsConstructor
public class RateController {
    private final MongoDBService mongoDBService;

    @GetMapping
    public ResponseEntity<?> getCurrentRate() {
        return new ResponseEntity<>(mongoDBService.getCurrentRate(),
                HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRates(){
        return new ResponseEntity<>(mongoDBService.getAllRates(), HttpStatus.OK);
    }

    @GetMapping("/last")
    public ResponseEntity<?> getLastRate(){
        return new ResponseEntity<>(mongoDBService.getLastRateResponse(), HttpStatus.OK);
    }
}
