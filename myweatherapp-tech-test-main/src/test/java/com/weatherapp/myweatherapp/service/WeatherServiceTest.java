package com.weatherapp.myweatherapp.service;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class WeatherServiceTest {

    @Mock
    private VisualcrossingRepository weatherRepo;  // Mock repository

    @InjectMocks
    private WeatherService weatherService;  // Inject mock into WeatherService

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for daylight hours comparison
    @Test
    void CompareDaylightHours_city2WithLongerDay() {
        CityInfo city1 = mockCityInfo("06:00:00", "18:00:00");  // 12 hours
        CityInfo city2 = mockCityInfo("06:30:00", "19:00:00");  // 12.5 hours

        when(weatherRepo.getByCity("London")).thenReturn(city1);
        when(weatherRepo.getByCity("Paris")).thenReturn(city2);

        String result = weatherService.compareDaylightHours("London", "Paris");

        assertEquals("Paris has the longest daylight hours.", result);
    }

    @Test
    void CompareDaylightHours_EqualDaylight() {
        CityInfo city1 = mockCityInfo("07:00:00", "19:00:00");  // 12 hours
        CityInfo city2 = mockCityInfo("07:00:00", "19:00:00");  // 12 hours

        when(weatherRepo.getByCity("New York")).thenReturn(city1);
        when(weatherRepo.getByCity("Los Angeles")).thenReturn(city2);

        String result = weatherService.compareDaylightHours("New York", "Los Angeles");

        assertEquals("Both cities have equal daylight hours.", result);
    }

    // Test for raincheck
    @Test
    void CheckRain_OnlyOneCityRaining() {
        CityInfo rainyCity = mockCityInfoWithCondition("Rain");
        CityInfo dryCity = mockCityInfoWithCondition("Clear");

        when(weatherRepo.getByCity("Tokyo")).thenReturn(rainyCity);
        when(weatherRepo.getByCity("Moscow")).thenReturn(dryCity);

        String result = weatherService.checkRain("Tokyo", "Moscow");

        assertEquals("It is currently raining in Tokyo.", result);
    }

    @Test
    void CheckRain_BothCitiesRaining() {
        CityInfo city1 = mockCityInfoWithCondition("Rain");
        CityInfo city2 = mockCityInfoWithCondition("Rain");

        when(weatherRepo.getByCity("Berlin")).thenReturn(city1);
        when(weatherRepo.getByCity("Dublin")).thenReturn(city2);

        String result = weatherService.checkRain("Berlin", "Dublin");

        assertEquals("It is currently raining in both Berlin and Dublin.", result);
    }

    @Test
    void CheckRain_BothCitiesNotRaining() {
        CityInfo city1 = mockCityInfoWithCondition("Clear");
        CityInfo city2 = mockCityInfoWithCondition("Clear");

        when(weatherRepo.getByCity("Rome")).thenReturn(city1);
        when(weatherRepo.getByCity("Madrid")).thenReturn(city2);

        String result = weatherService.checkRain("Rome", "Madrid");

        assertEquals("It is not raining in either city.", result);
    }

    // Helper methods for mocking data
    private CityInfo mockCityInfo(String sunrise, String sunset) {
        CityInfo cityInfo = new CityInfo();
        CityInfo.CurrentConditions conditions = new CityInfo.CurrentConditions();
        conditions.setSunrise(sunrise);
        conditions.setSunset(sunset);
        cityInfo.setCurrentConditions(conditions);
        return cityInfo;
    }

    private CityInfo mockCityInfoWithCondition(String condition) {
        CityInfo cityInfo = new CityInfo();
        CityInfo.CurrentConditions conditions = new CityInfo.CurrentConditions();
        conditions.setConditions(condition);
        cityInfo.setCurrentConditions(conditions);
        return cityInfo;
    }
}


