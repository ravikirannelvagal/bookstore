package com.task.bookstore.service;

import com.task.bookstore.exception.BookNotFoundException;
import com.task.bookstore.model.Book;
import com.task.bookstore.model.BookDetail;
import com.task.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class BookstoreService {

    private final BookRepository bookRepository;
    private final BookFinder bookFinder;

    @Autowired
    public BookstoreService(BookRepository bookRepository, BookFinder bookFinder) {
        this.bookRepository = bookRepository;
        this.bookFinder = bookFinder;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        Iterable<Book> all = bookRepository.findAll();
        all.forEach(books::add);
        return books;
    }

    public BookDetail getBookDetails(String id) {
        Book bookById = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        List<Book> similarBooks = bookFinder.findSimilarBooksById(bookById.getId());
        similarBooks.sort(Comparator.comparingDouble(Book::getSimilarityScore).reversed());
        return new BookDetail(bookById, similarBooks);
    }
}
