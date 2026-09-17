package com.adventure.book.validator;

public record ValidationError(
        ErrorType type,
        String detail
) { }
