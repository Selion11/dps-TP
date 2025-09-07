package edu.itba.converter.exchange.interfaces;

import edu.itba.converter.exchange.models.Currency;
import edu.itba.converter.exchange.models.Rate;
import edu.itba.converter.exchange.exception.UnavailableRateService;

import java.util.Date;
import java.util.List;

public interface RateGetter {

    List<Rate> getCurrentRate(Currency fromCurrency, List<Currency> toCurrency)  throws UnavailableRateService;

    List<Rate> getHistoricalRate(Currency fromCurrency, List<Currency> toCurrency, Date date) throws UnavailableRateService;

}
