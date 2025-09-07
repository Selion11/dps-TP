package edu.itba.converter.exchange;

import edu.itba.converter.exchange.exception.UnableToConvertException;
import edu.itba.converter.exchange.exception.UnavailableRateService;
import edu.itba.converter.exchange.interfaces.RateGetter;
import edu.itba.converter.exchange.interfaces.Converter;

import java.util.Date;
import java.util.List;

public class BalanceConverter implements Converter{
	private final RateGetter rateGetter;

	public BalanceConverter(final RateGetter rateGetter) {
		this.rateGetter = rateGetter;
	}

	private List<Conversion> convertFromRates(Balance fromBalance, List<Rate> rates){
		return rates.stream()
				.map(rate ->
						new Conversion(
								new Balance(
										rate.currency(),
										rate.rate().multiply(fromBalance.amount())),
								rate.rate())
				).toList();
	}

	public List<Conversion> convert(Balance fromBalance, List<Currency> toCurrency) throws UnableToConvertException {
		try {
			List<Rate> rates = this.rateGetter.getCurrentRate(fromBalance.currency(), toCurrency);
			return convertFromRates(fromBalance, rates);
		} catch (final UnavailableRateService e) {
			throw new UnableToConvertException(e.getMessage());
		}
	}

	public List<Conversion> getHistoricalRates(Balance fromBalance, List<Currency> toCurrency, Date conversionRate) throws UnableToConvertException {
		try {
			List<Rate> rates = this.rateGetter.getHistoricalRate(fromBalance.currency(), toCurrency, conversionRate);
			return convertFromRates(fromBalance, rates);
		} catch (final UnavailableRateService e) {
			throw new UnableToConvertException(e.getMessage());
		}
	}
}
