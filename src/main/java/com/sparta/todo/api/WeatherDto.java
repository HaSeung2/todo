package com.sparta.todo.api;

import lombok.Getter;


@Getter
public class WeatherDto {
    private String date;
    private String weather;

    public WeatherDto(String date, String weather) {
        this.date = date;
        this.weather = weather;
    }
}
