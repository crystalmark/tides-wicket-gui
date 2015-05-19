package uk.co.crystalmark.services;

import es.tidetim.tideengine.models.TimedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class TidesService {

    @Autowired
    private RestTemplate restTemplate;

    public List<TimedValue> getToday(String location) {
        TimedValue[] tides = restTemplate.getForObject("http://localhost:8080/tides/" + location, TimedValue[].class);
        return Arrays.asList(tides);
    }

    public List<TimedValue> getHourly(String location) {
        TimedValue[] tides = restTemplate.getForObject("http://localhost:8080/tides/hourly/" + location, TimedValue[].class);
        return Arrays.asList(tides);
    }
}
