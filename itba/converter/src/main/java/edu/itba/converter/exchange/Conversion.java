package edu.itba.converter.exchange;

import java.math.BigDecimal;

public record Conversion(Balance balance,BigDecimal rate) {
}
