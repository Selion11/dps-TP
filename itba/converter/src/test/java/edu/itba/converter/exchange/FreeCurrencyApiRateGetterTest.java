package edu.itba.converter.exchange;

import edu.itba.converter.exchange.exception.UnavailableRateService;
import edu.itba.converter.exchange.implementations.FreeCurrencyApiRateGetter;
import edu.itba.converter.exchange.interfaces.HttpClient;
import edu.itba.converter.exchange.interfaces.JsonParser;
import edu.itba.converter.exchange.models.Currency;
import edu.itba.converter.exchange.models.HttpResponse;
import edu.itba.converter.exchange.models.Rate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        edu.itba.converter.exchange.models.Currency from = new edu.itba.converter.exchange.models.Currency("USD");
        List<edu.itba.converter.exchange.models.Currency> to = List.of(new edu.itba.converter.exchange.models.Currency("EUR"), new edu.itba.converter.exchange.models.Currency("JPY"));

        HttpResponse response = new HttpResponse(200, "{\"data\": {\"EUR\": 0.93, \"JPY\": 146.5}}");

        when(httpClient.get(anyString(), anyMap(), anyMap())).thenReturn(response);

        Map<edu.itba.converter.exchange.models.Currency, BigDecimal> parsedRates = new HashMap<>();
        parsedRates.put(new edu.itba.converter.exchange.models.Currency("EUR"), BigDecimal.valueOf(0.93));
        parsedRates.put(new edu.itba.converter.exchange.models.Currency("JPY"), BigDecimal.valueOf(146.5));
        when(jsonParser.parseActual(response)).thenReturn(parsedRates);

        //WHEN
        List<Rate> result = rateGetter.getCurrentRate(from, to);

        //THEN
        assertTrue(result.contains(new Rate(new edu.itba.converter.exchange.models.Currency("EUR"), BigDecimal.valueOf(0.93))));
        assertTrue(result.contains(new Rate(new edu.itba.converter.exchange.models.Currency("JPY"), BigDecimal.valueOf(146.5))));
    }

    @Test
    void testGetCurrentRate_non200StatusThrows() {
        //GIVEN
        edu.itba.converter.exchange.models.Currency from = new edu.itba.converter.exchange.models.Currency("USD");
        List<edu.itba.converter.exchange.models.Currency> to = List.of(new edu.itba.converter.exchange.models.Currency("EUR"));
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
        edu.itba.converter.exchange.models.Currency from = new edu.itba.converter.exchange.models.Currency("USD");
        List<edu.itba.converter.exchange.models.Currency> to = List.of(new edu.itba.converter.exchange.models.Currency("EUR"), new edu.itba.converter.exchange.models.Currency("JPY"));
        Date date = new GregorianCalendar(2023, Calendar.SEPTEMBER, 1).getTime();

        HttpResponse response = new HttpResponse(200, "{\"data\": {\"2023-09-01\": {\"EUR\": 0.94, \"JPY\": 147.8}}}");

        when(httpClient.get(anyString(), anyMap(), anyMap())).thenReturn(response);

        Map<edu.itba.converter.exchange.models.Currency, BigDecimal> parsedRates = new HashMap<>();
        parsedRates.put(new edu.itba.converter.exchange.models.Currency("EUR"), BigDecimal.valueOf(0.94));
        parsedRates.put(new edu.itba.converter.exchange.models.Currency("JPY"), BigDecimal.valueOf(147.8));
        when(jsonParser.parseHistorical(response)).thenReturn(parsedRates);

        //WHEN
        List<Rate> result = rateGetter.getHistoricalRate(from, to, date);

        //THEN
        assertTrue(result.contains(new Rate(new edu.itba.converter.exchange.models.Currency("EUR"), BigDecimal.valueOf(0.94))));
        assertTrue(result.contains(new Rate(new edu.itba.converter.exchange.models.Currency("JPY"), BigDecimal.valueOf(147.8))));
    }


    @Test
    void testGetHistoricalRate_non200StatusThrows() {
        //GIVEN
        edu.itba.converter.exchange.models.Currency from = new edu.itba.converter.exchange.models.Currency("USD");
        List<edu.itba.converter.exchange.models.Currency> to = List.of(new Currency("EUR"));
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