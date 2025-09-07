package edu.itba.converter.exchange.models;

import java.math.BigDecimal;

public record Rate(Currency currency, BigDecimal rate) {
}
