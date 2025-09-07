package edu.itba.converter.exchange.interfaces;

import edu.itba.converter.exchange.models.Currency;
import edu.itba.converter.exchange.models.HttpResponse;

import java.math.BigDecimal;
import java.util.Map;

public interface JsonParser {
    Map<Currency, BigDecimal> parseActual (HttpResponse response);
    Map<Currency, BigDecimal> parseHistorical (HttpResponse response);
}
