package com.task.bookstore.controller;

import com.task.bookstore.exception.BookNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(value = BookNotFoundException.class)
    public String exception(BookNotFoundException exception, Model model) {
        log.warn(exception.getMessage(), exception);
        model.addAttribute("error", "Book not found.");
        return "error";
    }

    @ExceptionHandler(value = Exception.class)
    public String exception(Exception exception, Model model) {
        log.error(exception.getMessage(), exception);
        model.addAttribute("error", exception.getMessage());
        return "error";
    }
}
