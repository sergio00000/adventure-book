# Adventure Book API

REST API to browse and validate choose-your-own-adventure books.

## Requirements
Java 21

## Build and run
```
./mvnw spring-boot:run
```

The API starts on http://localhost:8080

Books are loaded from `src/main/resources/books` at startup. 
Each book is validated as it is loaded and the result is logged. 
Note that all three provided books are invalid, and `dragon-quest.json` is empty and is skipped.

## Validation

A book is considered invalid if any of these conditions is met:

- it has no beginning, or more than one
- it has no ending (multiple endings are allowed)
- it has an option pointing to a section id that does not exist
- a non-ending section has no options

Validation runs once at startup. 
The result is available on the `validationResult` field returned by `GET /books/{id}`.

## Endpoints

Book IDs are derived from the JSON filenames in `src/main/resources/books`
(for example `the-prisoner`, `crystal-caverns`, `pirates-jade-sea`).

#### Search books

`GET /books` returns all books, optionally filtered by title, author, category or
difficulty. Filters can be combined, and difficulty accepts `EASY`, `MEDIUM` or `HARD`.

```
curl "http://localhost:8080/books"
curl "http://localhost:8080/books?difficulty=MEDIUM"
curl "http://localhost:8080/books?author=Evelyn&difficulty=EASY"
```

#### Get book by id

`GET /books/{id}` returns the full details of a book, including its categories,
its sections and the validation result. Returns 404 if the id is unknown.

```
curl "http://localhost:8080/books/the-prisoner"
```

#### Add category

`POST /books/{id}/categories` adds a category to a book. The category is sent in
the request body, and the updated book is returned so the change can be verified
without a second call. Returns 404 if the id is unknown.

```
curl -X POST "http://localhost:8080/books/the-prisoner/categories" \
     -H "Content-Type: application/json" \
     -d '{"category": "HORROR"}'
```

#### Remove category

`DELETE /books/{id}/categories/{category}` removes a category from a book. The
updated book is returned. Returns 404 if the id is unknown.

```
curl -X DELETE "http://localhost:8080/books/the-prisoner/categories/HORROR"
```

## Objectives

- Objective 1: done
- Objective 2: done
- Objectives 3 to 6: not implemented, due to the time available
- 
  I chose to deliver two objectives fully working rather than four partially done.

## Design decisions

**No database, no JPA.** The books come as JSON files and there is nothing to keep
between restarts, so I load everything into a Map in memory at startup. If game
sessions had to survive a restart, or if the app ran as several instances, a
database would be the right call.

**Book IDs come from the filename.** The JSON files have no id of their own. I did
not use the title or the author because neither is unique. The same author can
write more than one book, and two books with the same key would silently overwrite
each other in the map. The filename is unique by construction and gives readable
URLs like /books/the-prisoner.

**Validation runs once, at startup.** The books never change after being loaded, so
there is no point validating on every request. The loader validates each book and
stores the result on it, and the API exposes it so a consumer can see whether a book
is playable and why not.

**Reading files through Spring's Resource.** The books are read with
PathMatchingResourcePatternResolver instead of java.io.File. Inside a packaged JAR
there is no filesystem to point at, so File would work while running from the IDE
and fail once the app is packaged. Resource works in both cases because it gives
back an InputStream rather than a path.

**The loader does not stop on a broken file.** A file that fails to parse is logged
and skipped. One of the provided files, dragon-quest.json, is empty, so this happens
on every startup.

**HTTP stays in the controllers.** The services return Optional<Book> and the
controllers turn that into 200 or 404. This way the services do not depend on
anything web-related and can be tested on their own.

**Repositories have no interface.** Each one has a single implementation and their
methods are different from each other, so an interface would only add indirection.
The services do have interfaces, which is the convention I am used to. I know this
is inconsistent, and in a real project I would pick one approach for both.

## Known limitations

- Objectives 3 to 6 are not implemented. PlayController, PlayService and
  PlaySessionRepository are there as empty scaffolding for objective 3.
- The endpoints return the entities directly instead of DTOs, so GET /books/{id}
  also returns the whole section graph, which is more than a caller needs.
- Nothing stops the same category from being added twice. A Set, or a check before
  adding, would fix it.
- Duplicate section ids are dropped silently (first one wins). The exercise does not
  list this as an invalidity rule, but a book like that is broken and I would report
  it as a validation error.
- There are no automated tests. BookValidator would be the first thing I would cover,
  since it has no state and no dependencies.
- All three provided books are invalid, so even with objective 3 done, none of them
  could be played from start to finish.