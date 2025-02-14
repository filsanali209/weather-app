package com.weatherapp.myweatherapp.service;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class WeatherService {

  @Autowired
  VisualcrossingRepository weatherRepo;

  public CityInfo forecastByCity(String city) {

    return weatherRepo.getByCity(city);
  }

  public String compareDaylightHours(String city1, String city2) {
    CityInfo cityInfo1 = forecastByCity(city1);
    CityInfo cityInfo2 = forecastByCity(city2);

    // calculate total daylight hours for both cities
    long daylightHoursCity1 = calculateDaylightHours(
      cityInfo1.getCurrentConditions().getSunrise(),
      cityInfo1.getCurrentConditions().getSunset()
    );

    long daylightHoursCity2 = calculateDaylightHours(
      cityInfo2.getCurrentConditions().getSunrise(),
      cityInfo2.getCurrentConditions().getSunset()
    );

    if (daylightHoursCity1 > daylightHoursCity2) {
      return city1 + " has the longest daylight hours.";
    } else if (daylightHoursCity2 > daylightHoursCity1) {
      return city2 + " has the longest daylight hours.";
    } else {
      return "Both cities have equal daylight hours.";
    }
  }

  public String checkRain(String city1, String city2) {
    CityInfo cityinfo1 = forecastByCity(city1);
    CityInfo cityinfo2 = forecastByCity(city2);

    //check for whether it is raining in each city
    boolean isRaining1 = isRaining(cityinfo1);
    boolean isRaining2 = isRaining(cityinfo2);

    if (isRaining1 && isRaining2) {
      return "It is currently raining in both " + city1 + " and " + city2 + ".";
    } else if (isRaining1) {
      return "It is currently raining in " + city1 + ".";
    } else if (isRaining2) {
      return "It is currently raining in " + city2 + ".";
    } else {
      return "It is not raining in either city.";
    }
  }

  private long calculateDaylightHours(String sunrise, String sunset) {
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
      Date sunriseTime = formatter.parse(sunrise);
      Date sunsetTime = formatter.parse(sunset);

      return (sunsetTime.getTime() - sunriseTime.getTime()) / (1000 * 60);
    } catch (Exception e) {
      throw new RuntimeException("Error calculating daylight duration.");
    }

    }

    private boolean isRaining(CityInfo cityinfo) {
      return cityinfo.getCurrentConditions().getConditions().toLowerCase().contains("rain");
    }



  }
