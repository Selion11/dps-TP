package edu.itba.converter.exchange;

import com.google.gson.Gson;
import edu.itba.converter.exchange.implementations.GsonJsonParser;
import edu.itba.converter.exchange.models.Currency;
import edu.itba.converter.exchange.models.HttpResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
                                    "data": {
                                        "2020-11-20": {
                                            "USD": 1.0,
                                            "EUR": 0.8435,
                                            "JPY": 103.83966
                                        }
                                    }
                                }
                """;
        HttpResponse response = new HttpResponse(200, json);

        //WHEN
        Map<Currency, BigDecimal> rates = parser.parseHistorical(response);


        //THEN
        assertEquals(BigDecimal.valueOf(1.0), rates.get(new Currency("USD")));
        assertEquals(BigDecimal.valueOf(0.8435), rates.get(new Currency("EUR")));
        assertEquals(BigDecimal.valueOf(103.83966), rates.get(new Currency("JPY")));
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