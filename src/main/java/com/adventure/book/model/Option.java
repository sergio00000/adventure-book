package com.adventure.book.model;

public record Option(
        String description,
        int gotoId,
        Consequence consequence
) { }
