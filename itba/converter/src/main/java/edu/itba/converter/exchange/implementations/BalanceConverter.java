package edu.itba.converter.exchange.implementations;

import edu.itba.converter.exchange.exception.UnableToConvertException;
import edu.itba.converter.exchange.exception.UnavailableRateService;
import edu.itba.converter.exchange.interfaces.Converter;
import edu.itba.converter.exchange.interfaces.RateGetter;
import edu.itba.converter.exchange.models.Balance;
import edu.itba.converter.exchange.models.Conversion;
import edu.itba.converter.exchange.models.Currency;
import edu.itba.converter.exchange.models.Rate;

import java.util.Date;
import java.util.List;

public class BalanceConverter implements Converter {
    private final RateGetter rateGetter;

    public BalanceConverter(final RateGetter rateGetter) {
        this.rateGetter = rateGetter;
    }

    private List<Conversion> convertFromRates(Balance fromBalance, List<Rate> rates) {
        return rates.stream()
                .map(rate ->
                        new Conversion(
                                new Balance(
                                        rate.currency(),
                                        rate.rate().multiply(fromBalance.amount())),
                                rate.rate())
                ).toList();
    }

    private List<Conversion> convertWithRates(Balance fromBalance, List<Currency> toCurrency, Date conversionRate) throws UnableToConvertException {
        try {
            List<Rate> rates = (conversionRate == null)
                    ? this.rateGetter.getCurrentRate(fromBalance.currency(), toCurrency)
                    : this.rateGetter.getHistoricalRate(fromBalance.currency(), toCurrency, conversionRate);
            return convertFromRates(fromBalance, rates);
        } catch (final UnavailableRateService e) {
            throw new UnableToConvertException(e.getMessage());
        }
    }

    public List<Conversion> convert(Balance fromBalance, List<Currency> toCurrency) throws UnableToConvertException {
        return convertWithRates(fromBalance, toCurrency, null);
    }

    public List<Conversion> convertHistorical(Balance fromBalance, List<Currency> toCurrency, Date conversionRate) throws UnableToConvertException {
        return convertWithRates(fromBalance, toCurrency, conversionRate);
    }
}
