package com.task.bookstore.service;

import com.task.bookstore.model.Book;
import com.task.bookstore.model.BooksView;
import com.task.bookstore.model.User;
import com.task.bookstore.repository.BookRepository;
import com.task.bookstore.repository.BookViewsRepository;
import com.task.bookstore.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataCollectorService {

    private final BookRepository bookRepository;
    private final BookViewsRepository bookViewsRepository;
    private final UserRepository userRepository;

    @Autowired
    public DataCollectorService(BookRepository bookRepository,
                                BookViewsRepository bookViewsRepository,
                                UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.bookViewsRepository = bookViewsRepository;
        this.userRepository = userRepository;
    }

    public void collectData() {
        File booksFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("books.csv")).getFile());
        File bookViewsFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("books-views.csv")).getFile());

        List<String> booksCsvList = readCsvRecords(booksFile);
        List<String> booksViewsCsvList = readCsvRecords(bookViewsFile);

        List<BooksView> booksViewList = booksViewsCsvList.stream().map(this::parseBooksViewsRecord).collect(Collectors.toList());
        Set<User> users = booksViewList.stream().map(BooksView::getUserId).map(User::new).collect(Collectors.toSet());
        booksViewList.forEach(bookViewsRepository::save);
        userRepository.saveAll(users);
        log.info("users saved in repository.");

        booksCsvList.stream().map(this::parseBooksRecord).forEach(bookRepository::save);
    }

    private Book parseBooksRecord(String csvLine) {
        String[] csvRecord = csvLine.split(";");
        Book book = new Book();
        double price = 0d;
        if (csvRecord[3].contains(",")) {
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            try {
                price = nf.parse(csvRecord[3]).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            price = Double.parseDouble(csvRecord[3]);
        }

        String bookId = csvRecord[0];

        book.setId(bookId);
        book.setName(csvRecord[1].replaceAll("\"", ""));
        book.setDetails(csvRecord[2].replaceAll("\"", ""));
        book.setPrice(price);
        book.setImage(csvRecord[4]);

        Iterable<User> allUsers = userRepository.findAll();
        List<Long> bookViewsVector = new ArrayList<>();
        for (User user : allUsers) {
            bookViewsVector.add(bookViewsRepository.findCountOfBookByUserId(user.getUserId(), bookId));
        }
        book.setViewVector(bookViewsVector.stream().map(String::valueOf).collect(Collectors.joining(",")));

        log.info("Book record parsed from csv file.");

        return book;
    }

    private BooksView parseBooksViewsRecord(String csvLine) {
        String[] csvRecord = csvLine.split(";");

        BooksView booksView = new BooksView();
        booksView.setBookId(csvRecord[0]);
        booksView.setUserId(csvRecord[1]);

        log.info("Book views record parsed from csv file");

        return booksView;
    }


    private List<String> readCsvRecords(File csvFile) {
        List<String> csvList = new ArrayList<>();
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                csvList.add(line);
            }
        } catch (Exception e) {
            log.error("Exception occurred while reading from buffered reader", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("Exception occurred while closing buffered reader", e);
                }
            }
        }

        csvList = csvList.subList(1, csvList.size());
        return csvList;
    }
}
