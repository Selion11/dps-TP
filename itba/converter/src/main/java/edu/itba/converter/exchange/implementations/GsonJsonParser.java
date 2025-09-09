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

    private Map<Currency, BigDecimal> parseRates(HttpResponse response, Type rootType, String dataKey) {
        Map<String, ?> root = gson.fromJson(response.body(), rootType);
        Map<String, ?> data = (Map<String, ?>) root.get(dataKey);

        Map<Currency, BigDecimal> rates = new HashMap<>();
        data.forEach((type, value) -> {
            if (value instanceof Double) {
                rates.put(new Currency(type), BigDecimal.valueOf((Double) value));
            } else if (value instanceof Map) {
                ((Map<String, Double>) value).forEach((nestedType, nestedValue) ->
                        rates.put(new Currency(nestedType), BigDecimal.valueOf(nestedValue))
                );
            }
        });

        return rates;
    }

    @Override
    public Map<Currency, BigDecimal> parseActual(HttpResponse response) {
        Type rootType = new TypeToken<Map<String, Map<String, Double>>>() {}.getType();
        return parseRates(response, rootType, "data");
    }

    @Override
    public Map<Currency, BigDecimal> parseHistorical(HttpResponse response) {
        Type rootType = new TypeToken<Map<String, Map<String, Map<String, Double>>>>() {}.getType();
        return parseRates(response, rootType, "data");
    }

}
