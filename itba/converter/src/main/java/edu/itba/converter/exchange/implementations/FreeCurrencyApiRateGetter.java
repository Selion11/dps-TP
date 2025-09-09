package edu.itba.converter.exchange.implementations;

import edu.itba.converter.exchange.exception.UnavailableRateService;
import edu.itba.converter.exchange.interfaces.HttpClient;
import edu.itba.converter.exchange.interfaces.JsonParser;
import edu.itba.converter.exchange.interfaces.RateGetter;
import edu.itba.converter.exchange.models.Currency;
import edu.itba.converter.exchange.models.HttpResponse;
import edu.itba.converter.exchange.models.Rate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class FreeCurrencyApiRateGetter implements RateGetter {

    private final HttpClient httpClient;
    private final JsonParser jsonParser;
    private final SimpleDateFormat formatter;


    private final String baseUrl = "https://api.freecurrencyapi.com/v1/";
    private final String apiKey = "fca_live_7cyd8YJ9UocVUfp4QvXZSyFccdh3JRO2VVuJW4mc";

    public FreeCurrencyApiRateGetter(HttpClient httpClient, JsonParser jsonParser, SimpleDateFormat formatter) {
        this.httpClient = httpClient;
        this.jsonParser = jsonParser;
        this.formatter = formatter;
    }

    private static List<Rate> mapper(Map<Currency, BigDecimal> map) {
        return map.entrySet().stream().map(entry -> new Rate(entry.getKey(), entry.getValue())).toList();
    }

    private void validateResponse(HttpResponse response) throws UnavailableRateService {
        if (response.status() != 200) {
            String error = "Error: " + response.status() + "\n" + response.body();
            throw new UnavailableRateService(error);
        }
    }

    private List<Rate> fetchRates(String endpoint, Map<String, Object> queryParams, boolean isHistorical) throws UnavailableRateService {
        final var response = this.httpClient.get(baseUrl + endpoint, queryParams,
                Map.of("accept", "application/json", "apikey", apiKey));
        validateResponse(response);
        return isHistorical ? mapper(jsonParser.parseHistorical(response)) : mapper(jsonParser.parseActual(response));
    }

    @Override
    public List<Rate> getCurrentRate(Currency fromCurrency, List<Currency> toCurrency) throws UnavailableRateService {
        String currencies = toCurrency.stream()
                .map(Currency::type)
                .collect(Collectors.joining(","));
        return fetchRates("latest", Map.of("base_currency", fromCurrency.type(), "currencies", currencies), false);
    }

    @Override
    public List<Rate> getHistoricalRate(Currency fromCurrency, List<Currency> toCurrency, Date date) throws UnavailableRateService {
        String currencies = toCurrency.stream()
                .map(Currency::type)
                .collect(Collectors.joining(","));
        return fetchRates("historical", Map.of("date", formatter.format(date), "base_currency", fromCurrency.type(), "currencies", currencies), true);
    }

}
