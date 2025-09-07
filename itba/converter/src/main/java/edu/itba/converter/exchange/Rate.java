package edu.itba.converter.exchange;

import java.math.BigDecimal;

public record Rate(Currency currency, BigDecimal rate) {
}
