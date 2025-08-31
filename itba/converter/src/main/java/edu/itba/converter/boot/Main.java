package edu.itba.converter.boot;

import edu.itba.converter.exchange.Currency;
import edu.itba.converter.exchange.BalanceConverter;
import edu.itba.converter.exchange.httpclient.UnirestHttpClient;

public class Main {
	public static void main(String[] args) {
		final var httpClient = new UnirestHttpClient();
		final var converter = new BalanceConverter(httpClient);
		final Currency USD = new Currency();
		System.out.println(converter.convert("EUR", "USD", 100));

		//Converter.convert(Blanace,List<Currency>);
	}
}
