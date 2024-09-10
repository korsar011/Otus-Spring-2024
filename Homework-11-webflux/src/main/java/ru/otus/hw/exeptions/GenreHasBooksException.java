package ru.otus.hw.exeptions;

public class GenreHasBooksException extends RuntimeException {
    public GenreHasBooksException(String message) {
        super(message);
    }
}
