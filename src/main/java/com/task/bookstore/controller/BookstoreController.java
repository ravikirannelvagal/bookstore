package com.task.bookstore.controller;

import com.task.bookstore.model.Book;
import com.task.bookstore.model.BookDetail;
import com.task.bookstore.model.Login;
import com.task.bookstore.service.BookstoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class BookstoreController {

    private final BookstoreService bookstoreService;

    @Autowired
    public BookstoreController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @RequestMapping(path = "/")
    public String redirectToLogin(Model model) {
        model.addAttribute("command", new Login());
        return "redirect:/task/login";
    }


    @RequestMapping(path = "/task/login")
    public String showLogin(Model model) {
        model.addAttribute("command", new Login());
        return "login";
    }

    @RequestMapping(path = "/task/login", method = RequestMethod.POST)
    public String doLogin(@Valid @ModelAttribute("login") Login login, BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            session.setAttribute("loginStatus", "FAILED");
            session.setAttribute("loginMessage", bindingResult.getAllErrors().get(0).getDefaultMessage());
            model.addAttribute("command", new Login());

            return "login";
        }
        session.setAttribute("loginMessage", "");
        session.setAttribute("user", login.getUsername());
        return "redirect:/task/books";
    }

    @RequestMapping(path = "/task/books")
    public String getAllBooks(Model model) {
        List<Book> books = bookstoreService.getAllBooks();
        model.addAttribute("books", books);
        return "home";
    }

    @RequestMapping(path = "/task/details")
    public String getBookDetails(@RequestParam("id") String id, Model model) {
        BookDetail bookDetail = bookstoreService.getBookDetails(id);
        model.addAttribute("bookDetail", bookDetail);
        return "detail";
    }
}
