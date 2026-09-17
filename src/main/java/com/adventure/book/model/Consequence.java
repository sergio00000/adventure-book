package com.adventure.book.model;

import com.adventure.book.model.enums.ConsequenceType;

public record Consequence(
        ConsequenceType type,
        int value,
        String text
) { }
