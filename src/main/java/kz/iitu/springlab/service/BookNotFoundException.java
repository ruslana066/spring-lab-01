package kz.iitu.springlab.service;

public class BookNotFoundException extends RuntimeException {

    private final long id;

    public BookNotFoundException(long id) {
        super("Book not found: " + id);
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
