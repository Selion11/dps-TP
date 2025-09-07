package edu.itba.converter.exchange;

import edu.itba.converter.exchange.implementations.GsonJsonParser;

import com.google.gson.Gson;
import edu.itba.converter.exchange.models.Currency;
import edu.itba.converter.exchange.models.HttpResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GsonJsonParserTest {

    private final Gson gson = new Gson();
    private final GsonJsonParser parser = new GsonJsonParser(gson);

    @Test
    void testParseActual() {
        //GIVEN
        String json = """
        {
            "data": {
                "USD": 1.0,
                "EUR": 0.93,
                "JPY": 146.5
            }
        }
        """;
        HttpResponse response = new HttpResponse(200, json);

        //WHEN
        Map<Currency, BigDecimal> rates = parser.parseActual(response);

        //THEN
        assertEquals(BigDecimal.valueOf(1.0), rates.get(new Currency("USD")));
        assertEquals(BigDecimal.valueOf(0.93), rates.get(new Currency("EUR")));
        assertEquals(BigDecimal.valueOf(146.5), rates.get(new Currency("JPY")));
        assertEquals(3, rates.size());
    }

    @Test
    void testParseHistorical() {
        //GIVEN
        String json = """
        {
                "data":{
                    "USD": 1.0,
                    "EUR": 0.94,
                    "JPY": 147.8
                }
        }
        """;
        HttpResponse response = new HttpResponse(200, json);

        //WHEN
        Map<Currency, BigDecimal> rates = parser.parseHistorical(response);


        //THEN
        assertEquals(BigDecimal.valueOf(1.0), rates.get(new Currency("USD")));
        assertEquals(BigDecimal.valueOf(0.94), rates.get(new Currency("EUR")));
        assertEquals(BigDecimal.valueOf(147.8), rates.get(new Currency("JPY")));
        assertEquals(3, rates.size());
    }

    @Test
    void testParseActual_emptyData() {
        //GIVEN
        String json = """
        {
            "data": {}
        }
        """;
        HttpResponse response = new HttpResponse(200, json);
        //WHEN
        Map<Currency, BigDecimal> rates = parser.parseActual(response);

        //THEN
        assertTrue(rates.isEmpty());
    }

    @Test
    void testParseHistorical_nullData() {
        //GIVEN
        String json = """
        {
            "data": null
        }
        """;
        HttpResponse response = new HttpResponse(200, json);

        //WHEN
        Map<Currency, BigDecimal> rates = parser.parseHistorical(response);

        //THEN
        assertTrue(rates.isEmpty());
    }
}