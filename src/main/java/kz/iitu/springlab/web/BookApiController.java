package kz.iitu.springlab.web;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookService;
import kz.iitu.springlab.web.dto.BookMapper;
import kz.iitu.springlab.web.dto.BookRequest;
import kz.iitu.springlab.web.dto.BookResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import java.util.Locale;

@Tag(name = "Books", description = "The catalogue of books")
@RestController
@RequestMapping("/api/books")
public class BookApiController {

    private final BookService service;
    private final BookMapper mapper;

    public BookApiController(BookService service, BookMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // GET /api/books
    // 200 OK
    @GetMapping
    public List<BookResponse> list(
            @RequestParam(required = false) String author) {
        return mapper.toResponseList(service.findAll(author));
    }

    // GET /api/books/{id}
    // 200 OK or 404 Not Found
    @Operation(summary = "Find a book by its identifier")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "The book was found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No book with this id",
                    content = @Content(
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public BookResponse find(
            @Parameter(
                    description = "Identifier of the book",
                    example = "42"
            )
            @PathVariable long id) {
        return mapper.toResponse(service.findById(id));
    }

    // POST /api/books
    // 201 Created + Location header
    @PostMapping
    public ResponseEntity<BookResponse> create(
            @Valid @RequestBody BookRequest request) {

        Book saved = service.create(mapper.toDomain(request));

        return ResponseEntity
                .created(URI.create("/api/books/" + saved.id()))
                .body(mapper.toResponse(saved));
    }

    // PUT /api/books/{id}
    // 200 OK or 404 Not Found
    @PutMapping("/{id}")
    public BookResponse update(
            @PathVariable long id,
            @Valid @RequestBody BookRequest request) {

        return mapper.toResponse(
                service.update(id, mapper.toDomain(request))
        );
    }

    // DELETE /api/books/{id}
    // 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/books/search?q=clean
// 200 OK with results or empty array; 400 Bad Request if q is empty
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String q) {

        // Проверяем, что параметр q передан и не пустой
        if (q == null || q.isBlank()) {
            ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST,
                    "Query parameter 'q' must not be blank"
            );

            problem.setTitle("Invalid search query");

            return ResponseEntity.badRequest().body(problem);
        }

        // Приводим запрос к нижнему регистру
        String query = q.trim().toLowerCase(Locale.ROOT);

        // Ищем по названию книги или имени автора
        List<BookResponse> results = service.findAll(null).stream()
                .filter(book ->
                        book.title().toLowerCase(Locale.ROOT).contains(query)
                                || book.author().toLowerCase(Locale.ROOT).contains(query)
                )
                .map(mapper::toResponse)
                .toList();

        // Если ничего не найдено, вернётся 200 OK и []
        return ResponseEntity.ok(results);
    }
}