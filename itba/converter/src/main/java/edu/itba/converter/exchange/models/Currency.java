package edu.itba.converter.exchange.models;

public record Currency(String coin){
    public Currency {
        if (coin == null || coin.isBlank()) {
            throw new IllegalArgumentException("Currency code cannot be null or blank");
        }
    }
}
