package kz.iitu.springlab.catalog;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll(String author) {
        if (author == null) {
            return repository.findAll();
        }

        return repository.findAll().stream()
                .filter(book -> book.author().toLowerCase().contains(author.toLowerCase()))
                .toList();
    }

    public Book findById(long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException("Book not found: " + id));
    }

    public Book create(Book book) {
        return repository.save(book);
    }

    public boolean delete(long id) {
        return repository.deleteById(id);
    }

    public List<Book> search(String query) {
        return repository.findAll().stream()
                .filter(book -> book.title().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }
}