package edu.itba.converter.exchange.implementations;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.itba.converter.exchange.interfaces.JsonParser;
import edu.itba.converter.exchange.models.Currency;
import edu.itba.converter.exchange.models.HttpResponse;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class GsonJsonParser implements JsonParser {
    private final Gson gson;

    public GsonJsonParser(Gson gson) {
        this.gson = gson;
    }

    public Map<Currency, BigDecimal> parseActual(HttpResponse response) {
        Type rootType = new TypeToken<Map<String, Map<String, Double>>>() {
        }.getType();
        Map<String, Map<String, Double>> root = gson.fromJson(response.body(), rootType);
        Map<String, Double> data = root.get("data");

        Map<Currency, BigDecimal> rates = new HashMap<>();

        data.forEach((type, value) -> {
            Currency c = new Currency(type);
            rates.put(c, BigDecimal.valueOf(value));
        });

        return rates;
    }

    @Override
    public Map<Currency, BigDecimal> parseHistorical(HttpResponse response) {
        Type rootType = new TypeToken<Map<String, Map<String, Map<String, Double>>>>() {
        }.getType();
        Map<String, Map<String, Map<String, Double>>> root = gson.fromJson(response.body(), rootType);

        Map<Currency, BigDecimal> rates = new HashMap<>();

        Map<String, Map<String, Double>> data = root.get("data");

        data.forEach((date, i) -> {
            i.forEach((type, value) -> {
                Currency currency = new Currency(type);
                rates.put(currency, BigDecimal.valueOf(value));
            });
        });
        

        return rates;
    }

}
