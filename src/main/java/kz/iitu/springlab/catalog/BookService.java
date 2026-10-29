package kz.iitu.springlab.catalog;

import kz.iitu.springlab.service.BookNotFoundException;
import kz.iitu.springlab.service.DuplicateTitleException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll(String author) {
        if (author == null || author.isBlank()) {
            return repository.findAll();
        }

        return repository.findAll().stream()
                .filter(book -> book.author()
                        .toLowerCase()
                        .contains(author.toLowerCase()))
                .toList();
    }

    public Book findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public Book create(Book book) {
        checkDuplicate(book, null);
        return repository.save(book);
    }

    public Book update(long id, Book book) {
        // Проверяем, существует ли книга
        findById(id);

        // Проверяем, нет ли дубликата
        checkDuplicate(book, id);

        // Сохраняем новую версию с прежним ID
        Book updated = new Book(
                id,
                book.title(),
                book.author(),
                book.year(),
                book.genre(),
                book.createdAt()
        );

        return repository.save(updated);
    }

    public void delete(long id) {
        // Если книги нет, будет выброшено BookNotFoundException
        findById(id);
        repository.deleteById(id);
    }

    public List<Book> search(String query) {
        if (query == null || query.isBlank()) {
            return repository.findAll();
        }

        return repository.findAll().stream()
                .filter(book -> book.title()
                        .toLowerCase()
                        .contains(query.toLowerCase()))
                .toList();
    }

    private void checkDuplicate(Book book, Long excludedId) {
        boolean duplicate = repository.findAll().stream()
                .filter(existing -> excludedId == null
                        || !existing.id().equals(excludedId))
                .anyMatch(existing ->
                        existing.title().equalsIgnoreCase(book.title())
                                && existing.author()
                                .equalsIgnoreCase(book.author()));

        if (duplicate) {
            throw new DuplicateTitleException(book.title());
        }
    }
}