package com.devsenior.nata.model;
public class Book {
 private String title;
 private String id;
 private String author;
 private boolean isLoaned;

    public Book(String title, String id, String author) {
        this.title = title;
        this.id = id;
        this.author = author;
        this.isLoaned = false;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

   
    public boolean isLoaned() {
        return isLoaned;
    }

    public void loanBook() {
        isLoaned = true;
    }

    public void returnBook() {
        isLoaned = false;
    }

    @Override
    public String toString() {
        return "Book [title=" + title + ", id=" + id + ", author=" + author + "]";
    }
    
}


