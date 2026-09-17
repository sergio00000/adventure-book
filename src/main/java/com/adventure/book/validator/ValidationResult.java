package com.adventure.book.validator;

import java.util.List;

public record ValidationResult (
        boolean valid,
        List<ValidationError> errors
) { }
