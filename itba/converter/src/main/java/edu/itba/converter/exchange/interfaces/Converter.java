package edu.itba.converter.exchange.interfaces;

import edu.itba.converter.exchange.exception.UnableToConvertException;
import edu.itba.converter.exchange.models.Balance;
import edu.itba.converter.exchange.models.Conversion;
import edu.itba.converter.exchange.models.Currency;

import java.util.Date;
import java.util.List;


public interface Converter {

    List<Conversion> convert(Balance fromBalance, List<Currency> toCurrency) throws UnableToConvertException;

    List<Conversion> convertHistorical(Balance fromBalance, List<Currency> toCurrency, Date conversionRate) throws UnableToConvertException; //TODO un "converter" solo convierte. No da rates, hay q cambiar el nombre del metodo de convertir cosas con una cotizacion historica
}
