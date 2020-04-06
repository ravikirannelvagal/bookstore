package com.task.bookstore;

import com.task.bookstore.service.DataCollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

public class BookstoreDataCollector implements CommandLineRunner {

    @Autowired
    private DataCollectorService dataCollectorService;

    @Override
    public void run(String... args){
        dataCollectorService.collectData();
    }

}
