package edu.itba.converter.exchange.implementations;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import edu.itba.converter.exchange.models.Currency;
import edu.itba.converter.exchange.models.HttpResponse;
import edu.itba.converter.exchange.interfaces.JsonParser;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class GsonJsonParser implements JsonParser {
    private Gson gson;
    public GsonJsonParser(Gson gson){
        this.gson = gson;
    }
    public Map<Currency, BigDecimal> parseActual (HttpResponse response){

        Map<String,Object> root = gson.fromJson(response.body(), Map.class);

        Map<String,Double> data = (Map<String, Double>) root.get("data");

        Map<Currency,BigDecimal> rates = new HashMap<>();

        data.forEach((coin,value) -> {
            Currency c = new Currency(coin);
            rates.put(c, BigDecimal.valueOf(value));
        });

        return rates;
    }

    @Override
    public Map<Currency, BigDecimal> parseHistorical(HttpResponse response) {
        Type rootType = new TypeToken<Map<String, Map<String, Double>>>(){}.getType();
        Map<String, Map<String, Double>> root = gson.fromJson(response.body(), rootType);

        Map<Currency, BigDecimal> rates = new HashMap<>();

        Map<String, Double> data = root.get("data");
        if (data != null) {
            data.forEach((coin, value) -> {
                Currency currency = new Currency(coin);
                rates.put(currency, BigDecimal.valueOf(value));
            });
        }

        return rates;
    }

}
