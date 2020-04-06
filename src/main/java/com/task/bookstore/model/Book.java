package com.task.bookstore.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
@Entity
public class Book implements Serializable, Comparable<Book> {

    @Id
    private String id;
    private String name;
    private String details;
    private Double price;
    private String image;
    private String viewVector;

    @Transient
    private Double similarityScore;


    @Override
    public int compareTo(Book other) {
        return this.similarityScore.compareTo(other.similarityScore);
    }

}
