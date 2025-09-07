package edu.itba.converter.boot;

import com.google.gson.Gson;
import edu.itba.converter.exchange.models.Balance;
import edu.itba.converter.exchange.models.Currency;
import edu.itba.converter.exchange.implementations.BalanceConverter;
import edu.itba.converter.exchange.implementations.UnirestHttpClient;
import edu.itba.converter.exchange.implementations.GsonJsonParser;
import edu.itba.converter.exchange.implementations.FreeCurrencyApiRateGetter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class Main {
	public static void main(String[] args){
		final var httpClient = new UnirestHttpClient();
		final var jsonParser = new GsonJsonParser(new Gson());
		final var rateGetter = new FreeCurrencyApiRateGetter(httpClient, jsonParser, new SimpleDateFormat("yyyy-MM-dd"));
		final var converter = new BalanceConverter(rateGetter);
		final Currency Usd = new Currency("USD");
		final Balance fromBalanceUsd = new Balance(Usd, BigDecimal.valueOf(100));
		final Currency Eur = new Currency("EUR");
		final Currency Jpy = new Currency("JPY");
		LocalDate localDate = LocalDate.parse("2024-11-20");
		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		try {
			System.out.println("Actual Rates:");
			System.out.println("100 USD as EUR and JPY");
			converter.convert(fromBalanceUsd, List.of(Eur, Jpy))
					.forEach(b ->{
						System.out.println(b.balance().currency().coin() +" "+ b.balance().amount() + " USED rate: " + b.rate());
					});



			System.out.println("\nHistorical Rates:");

			System.out.println("100 USD to EUR and JPY in 2024-11-20");
			converter.getHistoricalRates(fromBalanceUsd, List.of(Eur, Jpy), date)
					.forEach(b ->{
						System.out.println(b.balance().currency().coin() +" "+ b.balance().amount() + " USED rate: " + b.rate()+'\n');
					});
		}	catch (final Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
