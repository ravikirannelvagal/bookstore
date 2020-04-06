package com.task.bookstore.service;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.task.bookstore.exception.BookNotFoundException;
import com.task.bookstore.model.Book;
import com.task.bookstore.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BookFinder {

    private final LoadingCache<String, List<Book>> cache;
    private final BookRepository bookRepository;
    private final CosineSimilarityService similarityService;


    @Autowired
    public BookFinder(BookRepository bookRepository, CosineSimilarityService similarityService) {
        this.bookRepository = bookRepository;
        this.similarityService = similarityService;
        cache = Caffeine.newBuilder()
                .maximumSize(10000)
                .refreshAfterWrite(5, TimeUnit.MINUTES)
                .build(this::loadCache);
    }

    List<Book> findSimilarBooksById(String id) {
        return cache.get(id);
    }

    @Nullable
    private List<Book> loadCache(@NonNull String key) {
        List<Book> suggestions = new ArrayList<>();
        log.info("Cache miss for key: " + key);
        synchronized (new Object()) {
            Book selectedBook = bookRepository.findById(key).orElseThrow(BookNotFoundException::new);
            Iterable<Book> bookIterable = bookRepository.findAll();

            bookIterable.forEach(suggestedBook -> addSuggestion(suggestions, selectedBook, suggestedBook));
        }

        return suggestions;
    }

    private void addSuggestion(List<Book> suggestions, Book selectedBook, Book suggestedBook) {
        if (suggestedBook != selectedBook) {
            log.info("suggesting similar book for " + selectedBook.getId());
            suggestions.add(calculateSimilarity(selectedBook, suggestedBook));
        }
    }

    private Book calculateSimilarity(Book selectedBook, Book suggestedBook) {
        String selectedBookVectorStr = selectedBook.getViewVector();
        String suggestedBookVectorStr = suggestedBook.getViewVector();

        suggestedBook.setSimilarityScore(
                similarityService.getSimilarityScore(parseStringToList(selectedBookVectorStr),
                        parseStringToList(suggestedBookVectorStr)));

        log.info("Book" + suggestedBook.getId() + "'s suggestion score: " + suggestedBook.getSimilarityScore());
        return suggestedBook;
    }

    private List<Integer> parseStringToList(String selectedBookVectorStr) {
        return Arrays.stream(selectedBookVectorStr.split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }
}
