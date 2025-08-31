package edu.itba.converter.exchange;

import java.util.List;
import java.util.Map;

public interface RateGetter {
    Map<Currency,Double> getCurrentRate(Currency fromCurrency, List<Currency> toCurrency);
}
