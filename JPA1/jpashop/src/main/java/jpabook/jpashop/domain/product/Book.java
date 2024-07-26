package jpabook.jpashop.domain.product;

import jakarta.persistence.Entity;
import jpabook.jpashop.domain.Item;

@Entity
public class Book extends Item {
    private String author;
    private String isbn;
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
