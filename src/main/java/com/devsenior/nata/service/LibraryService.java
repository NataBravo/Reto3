package com.devsenior.nata.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.devsenior.nata.BookRepository;
import com.devsenior.nata.LoanRepository;
import com.devsenior.nata.exceptions.NotFoundException;
import com.devsenior.nata.model.Book;
import com.devsenior.nata.model.Loan;
import com.devsenior.nata.model.User;

public class LibraryService {
    private BookRepository bookRepository;
    private LoanRepository loanRepository;
    private List<User> users;
    private List<Loan> loans;

    public LibraryService(BookRepository bookRepository, LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.users = new ArrayList<>();
        this.loans = new ArrayList<>();

    }

    public void addLoanBook(User user, Book book) throws NotFoundException {

        if (book.isLoaned()) {
            throw new NotFoundException("El libro ya esta prestado");
        }
        book.loanBook();
        Loan loan = new Loan(user, book, LocalDate.now());
        loanRepository.save(loan);

    }

    public void returnBook(Book book) throws NotFoundException {

        if (!book.isLoaned()) {
            throw new NotFoundException("El libro no esta prestado");
        }
        book.returnBook();
    }

    public void addBook(Book book) {
        bookRepository.save(book);

    }

    public Book getBookById(String id) {
        return bookRepository.findById(id);
    }

    public void addUsers(String name, String id) {
        users.add(new User(name, id));
    }

    public List<Loan> getLoansByUserId(String userId) throws NotFoundException {
        loans = loanRepository.findLoanByUserId(userId);
        for (Loan loan : loans) {
            if (loan.getUser().getId().equals(userId)) {
                return loans;
            }
        }
        throw new NotFoundException("El usuario no tiene prestados libros");
    }

    public User getUsersById(String id) throws NotFoundException {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        throw new NotFoundException("El usuario no existe");
    }

}
