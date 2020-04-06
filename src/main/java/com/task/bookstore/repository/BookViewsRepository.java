package com.task.bookstore.repository;

import com.task.bookstore.model.BooksView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookViewsRepository extends PagingAndSortingRepository<BooksView, Long> {

    @Query("select COUNT(BV) from BooksView BV where userId= ?1 AND bookId = ?2")
    Long findCountOfBookByUserId(String userId, String bookId);
}
