package com.task.bookstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookDetail {
    private Book book;
    private List<Book> similarObjects;
}
