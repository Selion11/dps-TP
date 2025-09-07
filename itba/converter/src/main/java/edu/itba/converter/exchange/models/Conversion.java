package edu.itba.converter.exchange.models;

import java.math.BigDecimal;

public record Conversion(Balance balance, BigDecimal rate) {
}
