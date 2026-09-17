package com.adventure.book.model;

import com.adventure.book.model.enums.SectionType;

import java.util.List;

public record Section(
        int id,
        String text,
        SectionType type,
        List<Option> options
) { }
