package com.devsenior.nata.model;

import java.time.LocalDate;

public class Loan {
    private User user;
    private Book book;
    private LocalDate loanDate;

    public Loan(User user, Book book, LocalDate loanDate) {
        this.user = user;    
        this.book = book;
        this.loanDate = LocalDate.now();
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    
}
