package edu.itba.converter.exchange;

import edu.itba.converter.exchange.exception.UnableToConvertException;
import edu.itba.converter.exchange.exception.UnavailableRateService;
import edu.itba.converter.exchange.interfaces.RateGetter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BalanceConverterTest {

    private static final BigDecimal balance = BigDecimal.valueOf(123.45);
    private static final BigDecimal rateEur = BigDecimal.valueOf(0.90);
    private static final BigDecimal rateArs = BigDecimal.valueOf(950.00);

    @Test
    void convertSuccessMultipleCurrencies() throws Exception {
        // Given
        RateGetter rateGetter = mock(RateGetter.class);
        BalanceConverter converter = new BalanceConverter(rateGetter);

        Currency usd = new Currency("USD");
        Currency eur = new Currency("EUR");
        Currency ars = new Currency("ARS");

        Balance from = new Balance(usd, balance);
        List<Currency> targets = List.of(eur, ars);

        Conversion expectedEur = new Conversion(
                new Balance(eur, balance.multiply(rateEur)),
                rateEur);

        Conversion expectedArs = new Conversion(
                new Balance(ars, balance.multiply(rateArs)),
                rateArs);

        when(rateGetter.getCurrentRate(from.currency(), targets))
                .thenReturn(List.of(
                        new Rate(eur, new BigDecimal(rateEur.toString())),
                        new Rate(ars, new BigDecimal(rateArs.toString()))
                ));


        // When
        List<Conversion> result = converter.convert(from, targets);


        // Then

        // 123.45 * 0.90 = 111.105
        assertTrue(result.contains(expectedArs));

        // 123.45 * 950 = 117277.5
        assertTrue(result.contains(expectedEur));
    }

    @Test
    void convertWhenRateGetterFailsWrapsAndThrows() throws Exception {
        // Given
        RateGetter rateGetter = mock(RateGetter.class);
        BalanceConverter converter = new BalanceConverter(rateGetter);

        Currency usd = new Currency("USD");
        Currency eur = new Currency("EUR");

        Balance from = new Balance(usd, BigDecimal.ONE);
        List<Currency> targets = List.of(eur);

        // When
        when(rateGetter.getCurrentRate(from.currency(), targets))
                .thenThrow(new UnavailableRateService("Boom"));

        //Then
        assertThrows(UnableToConvertException.class, () -> converter.convert(from, targets));
    }
}
