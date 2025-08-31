package edu.itba.converter.exchange.rategetter;

import edu.itba.converter.exchange.Currency;
import edu.itba.converter.exchange.HttpClient;
import edu.itba.converter.exchange.RateGetter;

import java.util.List;
import java.util.Map;

public class FreeCurrencyApiRateGetter implements RateGetter {
    private final HttpClient httpClient;

    public FreeCurrencyApiRateGetter(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Map<Currency, Double> getCurrentRate(Currency fromCurrency, List<Currency> toCurrency) {
        // Query the API using API Key, base currency and target currency.
			final var response = this.httpClient.get("https://api.freecurrencyapi.com/v1/latest",
					Map.of("base_currency", fromCurrency, "currencies", toCurrency), Map.of("accept",
							"application/json", "apikey", "fca_live_tMQ4oYRmk8T587mrTdOFbTREYXjqCLRkXwJUS4C6"));

			// Check if the response is successful (status code 200).
			if (response.status() != 200) {
				System.err.println("Error: " + response.status());
			}

			// Parse the response body to a Java object.
			final var exchangeRateResponse = new Gson().fromJson(response.body(), ExchangeRateResponse.class);
        return Map.of();
    }
}
