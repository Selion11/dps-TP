package edu.itba.converter.exchange.models;

import java.math.BigDecimal;

public record Balance(Currency currency, BigDecimal amount) {

}
