package com.adventure.book.loader;

import com.adventure.book.model.Book;
import com.adventure.book.repository.BookRepository;
import com.adventure.book.validator.BookValidator;
import com.adventure.book.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.InputStream;
import java.util.Objects;

@Component
public class BookLoader implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(BookLoader.class);

    private final BookRepository repository;
    private final BookValidator validator;
    private final ObjectMapper objectMapper;

    public BookLoader(BookRepository repository, BookValidator validator) {
        this.repository = repository;
        this.validator = validator;
        this.objectMapper = JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] files = resolver.getResources("classpath*:books/*.json");

        for (Resource file : files) {
            String id = Objects.requireNonNull(file.getFilename()).replace(".json", "");

            try (InputStream inputStream = file.getInputStream()) {
                Book book = objectMapper.readValue(inputStream, Book.class);
                book.setId(id);

                ValidationResult result = validator.validate(book);
                book.setValidationResult(result);

                repository.save(book);

                if (result.valid()) {
                    logger.info("Saved Book with id '{}'", id);
                } else {
                    logger.warn("Loaded book '{}' but it is INVALID: {}", id, result.errors());
                }

            } catch (Exception e) {
                logger.warn("Could not load Book with id '{}' '{}'", id, e.getMessage());
            }
        }
        logger.info("Finished loading Books. The size of collection: {}", repository.findAll().size());
    }
}
