package edu.itba.converter.exchange;

import java.math.BigDecimal;


public record Balance(Currency currency, BigDecimal ammount) {
}
