package com.sparta.todo.api;

import com.sparta.todo.exception.CustomException;
import com.sparta.todo.exception.ErrorCode;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String getWeather() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM-dd");
        String formattedDate = today.format(format);

        URI uri = UriComponentsBuilder
                .fromUriString("https://f-api.github.io/f-api/weather.json")
                .encode()
                .build()
                .toUri();

        ResponseEntity<WeatherDto[]> response = restTemplate.getForEntity(uri, WeatherDto[].class);
        return forJsonToString(response,formattedDate);
    }

    private String forJsonToString(ResponseEntity<WeatherDto[]> response, String formattedDate) {
        try {
            for(int i = 0; i < response.getBody().length; i++) {
                if(response.getBody()[i].getDate().equals(formattedDate)) return response.getBody()[i].getWeather();
            }
        } catch (NullPointerException e) {
            throw new CustomException(ErrorCode.NO_API_DATA);
        }
        return "";
    }
}
