package uk.co.crystalmark.services;

import es.tidetim.tideengine.models.TimedValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class TidesService {

    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${tidetimes.url}")
    private String url;

    public List<TimedValue> getToday(String location) {
        TimedValue[] tides = restTemplate.getForObject(url+"tides/" + location, TimedValue[].class);
        return Arrays.asList(tides);
    }

    public List<TimedValue> getHourly(String location) {
        TimedValue[] tides = restTemplate.getForObject(url+"tides/hourly/" + location, TimedValue[].class);
        return Arrays.asList(tides);
    }

    public List<String> getStations() {
        String[] stations = restTemplate.getForObject(url+"tides/stations", String[].class);
        return Arrays.asList(stations);
    }
}
