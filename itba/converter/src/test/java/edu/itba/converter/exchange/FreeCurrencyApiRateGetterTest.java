package edu.itba.converter.exchange;

import edu.itba.converter.exchange.exception.UnavailableRateService;
import edu.itba.converter.exchange.interfaces.HttpClient;
import edu.itba.converter.exchange.interfaces.JsonParser;
import edu.itba.converter.exchange.rategetter.FreeCurrencyApiRateGetter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FreeCurrencyApiRateGetterTest {

    private HttpClient httpClient;
    private JsonParser jsonParser;
    private SimpleDateFormat formatter;
    private FreeCurrencyApiRateGetter rateGetter;

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        jsonParser = mock(JsonParser.class);
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        rateGetter = new FreeCurrencyApiRateGetter(httpClient, jsonParser, formatter);
    }

    @Test
    void testGetCurrentRate_success() throws UnavailableRateService {
        //GIVEN
        Currency from = new Currency("USD");
        List<Currency> to = List.of(new Currency("EUR"), new Currency("JPY"));

        HttpResponse response = new HttpResponse(200, "{\"data\": {\"EUR\": 0.93, \"JPY\": 146.5}}");

        when(httpClient.get(anyString(), anyMap(), anyMap())).thenReturn(response);

        Map<Currency, BigDecimal> parsedRates = new HashMap<>();
        parsedRates.put(new Currency("EUR"), BigDecimal.valueOf(0.93));
        parsedRates.put(new Currency("JPY"), BigDecimal.valueOf(146.5));
        when(jsonParser.parseActual(response)).thenReturn(parsedRates);

        //WHEN
        List<Rate> result = rateGetter.getCurrentRate(from, to);

        //THEN
        assertTrue(result.contains(new Rate(new Currency("EUR"), BigDecimal.valueOf(0.93))));
        assertTrue(result.contains(new Rate(new Currency("JPY"), BigDecimal.valueOf(146.5))));
    }

    @Test
    void testGetCurrentRate_non200StatusThrows() {
        //GIVEN
        Currency from = new Currency("USD");
        List<Currency> to = List.of(new Currency("EUR"));
        HttpResponse response = new HttpResponse(500, "Internal Server Error");

        when(httpClient.get(anyString(), anyMap(), anyMap())).thenReturn(response);

        //WHEN
        UnavailableRateService thrown = assertThrows(
                UnavailableRateService.class,
                () -> rateGetter.getCurrentRate(from, to)
        );

        //THEN
        assertTrue(thrown.getMessage().contains("Error: 500"));
    }

    @Test
    void testGetHistoricalRate_success() throws UnavailableRateService {
        //GIVEN
        Currency from = new Currency("USD");
        List<Currency> to = List.of(new Currency("EUR"), new Currency("JPY"));
        Date date = new GregorianCalendar(2023, Calendar.SEPTEMBER, 1).getTime();

        HttpResponse response = new HttpResponse(200, "{\"data\": {\"2023-09-01\": {\"EUR\": 0.94, \"JPY\": 147.8}}}");

        when(httpClient.get(anyString(), anyMap(), anyMap())).thenReturn(response);

        Map<Currency, BigDecimal> parsedRates = new HashMap<>();
        parsedRates.put(new Currency("EUR"), BigDecimal.valueOf(0.94));
        parsedRates.put(new Currency("JPY"), BigDecimal.valueOf(147.8));
        when(jsonParser.parseHistorical(response)).thenReturn(parsedRates);

        //WHEN
        List<Rate> result = rateGetter.getHistoricalRate(from, to, date);

        //THEN
        assertTrue(result.contains(new Rate(new Currency("EUR"), BigDecimal.valueOf(0.94))));
        assertTrue(result.contains(new Rate(new Currency("JPY"), BigDecimal.valueOf(147.8))));
    }


    @Test
    void testGetHistoricalRate_non200StatusThrows() {
        //GIVEN
        Currency from = new Currency("USD");
        List<Currency> to = List.of(new Currency("EUR"));
        Date date = new GregorianCalendar(2023, Calendar.SEPTEMBER, 1).getTime();
        HttpResponse response = new HttpResponse(500, "Internal Server Error");

        when(httpClient.get(anyString(), anyMap(), anyMap())).thenReturn(response);

        //WHEN
        UnavailableRateService thrown = assertThrows(
                UnavailableRateService.class,
                () -> rateGetter.getHistoricalRate(from, to, date)
        );

        //THEN
        assertTrue(thrown.getMessage().contains("Error: 500"));
    }
}