package com.adventure.book.validator;

import com.adventure.book.model.Book;
import com.adventure.book.model.Section;
import com.adventure.book.model.enums.SectionType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adventure.book.validator.ErrorType.*;
import static java.util.Objects.isNull;

@Component
public class BookValidator {

    public ValidationResult validate(Book book) {
        List<ValidationError> errors = new ArrayList<>();
        int countBegin = 0;
        int countEnd = 0;

        Map<Integer, Section> sectionById = new HashMap<>();
        for (Section section : book.getSections()) {
            sectionById.putIfAbsent(section.id(), section);

            if (SectionType.BEGIN.equals(section.type())) {
                countBegin++;
            }

            if (SectionType.END.equals(section.type())) {
                countEnd++;
            }

            if (!SectionType.END.equals(section.type()) && (isNull(section.options()) || section.options().isEmpty())) {
                errors.add(new ValidationError(SECTION_WITHOUT_OPTIONS, "Section " + section.id() + " is not an ending and has no options"));
            }
        }

        if (countBegin > 1) {
            errors.add(new ValidationError(MULTIPLE_BEGINS, "Sections must have only one begin"));
        }

        if (countBegin == 0) {
            errors.add(new ValidationError(NO_BEGIN, "Sections must have at least one begin"));
        }

        if (countEnd < 1) {
            errors.add(new ValidationError(NO_END, "Sections must have at least one end"));
        }

        for (Section section : book.getSections()) {
            if (isNull(section.options())) {
                continue;
            }
            section.options().forEach(option -> {
                if (!sectionById.containsKey(option.gotoId())) {
                    errors.add(new ValidationError(INVALID_GOTO_ID, "Section " + section.id() + " points to " + option.gotoId() + ", which does not exist"));
                }
            });
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }
}
