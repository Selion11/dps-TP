package edu.itba.converter.exchange;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class BalanceConverter {
	private final RateGetter rateGetter;

	public BalanceConverter(final HttpClient httpClient, final RateGetter rateGetter) {
		this.rateGetter = rateGetter;
	}

	public double convert(Balance fromBalance, List<Currency> toCurrency) {
		try {
//			// Query the API using API Key, base currency and target currency.
//			final var response = this.httpClient.get("https://api.freecurrencyapi.com/v1/latest",
//					Map.of("base_currency", fromCurrency, "currencies", toCurrency), Map.of("accept",
//							"application/json", "apikey", "fca_live_tMQ4oYRmk8T587mrTdOFbTREYXjqCLRkXwJUS4C6"));
//
//			// Check if the response is successful (status code 200).
//			if (response.status() != 200) {
//				System.err.println("Error: " + response.status());
//			}
//
//			// Parse the response body to a Java object.
//			final var exchangeRateResponse = new Gson().fromJson(response.body(), ExchangeRateResponse.class);
//
			Map<Currency,Double> rates = this.rateGetter.getCurrentRate(fromBalance.currency(), toCurrency);

			return fromBalance.ammount() * rates;
		} catch (final Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		return 0;
	}

	// Define a nested class to represent the response body.
	private static class ExchangeRateResponse {
		private Map<String, Double> data;

		public void setData(Map<String, Double> data) {
			this.data = data;
		}

		public double getExchange(final String toCurrency) {
			return this.data.get(toCurrency);
		}
	}

}
