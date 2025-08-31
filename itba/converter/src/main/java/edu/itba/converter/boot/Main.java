package edu.itba.converter.boot;

import edu.itba.converter.exchange.CurrencyConverter;
import edu.itba.converter.exchange.httpclient.UnirestHttpClient;

public class Main {
	public static void main(String[] args) {
		final var httpClient = new UnirestHttpClient();
		final var converter = new CurrencyConverter(httpClient);
		System.out.println(converter.convert("EUR", "USD", 100));
	}
}
