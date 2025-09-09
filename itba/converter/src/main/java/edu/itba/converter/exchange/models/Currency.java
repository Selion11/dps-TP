package edu.itba.converter.exchange.models;

public record Currency(String type) {
    public Currency {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Currency code cannot be null or blank");
        }
    }
}
