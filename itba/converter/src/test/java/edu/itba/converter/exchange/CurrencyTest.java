package edu.itba.converter.exchange;

import edu.itba.converter.exchange.models.Currency;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CurrencyTest {


    @Test
    void CurrencyNullTest() throws IllegalArgumentException{
        assertThrows(IllegalArgumentException.class, () -> new Currency(null));
    }

    @Test
    void CurrencyBlankTest() throws IllegalArgumentException{
        assertThrows(IllegalArgumentException.class, () -> new Currency(""));
    }
}
