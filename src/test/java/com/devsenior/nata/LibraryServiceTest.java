package com.devsenior.nata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import com.devsenior.nata.exceptions.NotFoundException;
import com.devsenior.nata.model.Book;
import com.devsenior.nata.model.Loan;
import com.devsenior.nata.model.User;
import com.devsenior.nata.service.LibraryService;

public class LibraryServiceTest {
    private LibraryService libraryService;
    private BookRepository bookRepository;
    private LoanRepository loanRepository;
    private Loan loan;

    @BeforeEach
    void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);
        loanRepository = Mockito.mock(LoanRepository.class);
        libraryService = new LibraryService(bookRepository, loanRepository);
        loan = Mockito.mock(Loan.class);
    }

    @Test
    void testAddBook() {
        // given
        Book book = new Book("Aprendiendo Java", "1", "DevSenior");

        // when
        libraryService.addBook(book);
        // then

        bookRepository.save(book);
        assertNotNull(book);
        assertEquals("Aprendiendo Java", book.getTitle());
        assertEquals("1", book.getId());
        assertEquals("DevSenior", book.getAuthor());

    }

    @Test
    void testAddLoanBook() throws NotFoundException {
        // given
        Book book = new Book("Aprendiendo Java", "1", "DevSenior");
        User user = new User("DevSenior", "1");
        // when

        libraryService.addLoanBook(user, book);
        loanRepository.save(new Loan(user, book, LocalDate.now()));
        Mockito.when(loan.getLoanDate()).thenReturn(LocalDate.now());
        // then
        assertTrue(book.isLoaned());
        assertEquals(loan.getLoanDate(), LocalDate.now());

    }

    @Test
    void addLoanBookWhenBookIsLoaned() throws NotFoundException {
        // given
        Book book = new Book("Aprendiendo Java", "1", "DevSenior");
        User user = new User("DevSenior", "1");
        book.loanBook();
        // when- then
        assertThrows(NotFoundException.class, () -> libraryService.addLoanBook(user, book));
    }

    @Test
    void testAddUsers() {

        // given
        String name = "DevSenior";
        String id = "1";
        User user = new User(name, id);
        // when

        libraryService.addUsers(name, id);
        // then

        assertNotNull(user);
        assertEquals(name, user.getName());
        assertEquals(id, user.getId());

    }

    @Test
    void testGetBookById() {
        // given
        String id = "1";
        Book book = new Book("Aprendiendo Java", id, "DevSenior");
        libraryService.addBook(book);
        // when
        Mockito.when(bookRepository.findById(id)).thenReturn(book);
        libraryService.getBookById(id);
        // then
        assertEquals(book, libraryService.getBookById(id));
    }

    @Test
    void testGetLoansByUserId() throws NotFoundException {
        // given
        Book book = new Book("Aprendiendo Java", "1", "DevSenior");
        Book book2 = new Book("Aprendiendo Java", "2", "Con Nata");
        String name = "DevSenior";
        String id = "1";
        User user = new User(name, id);
        libraryService.addBook(book);
        libraryService.addUsers(name, id);
        libraryService.addLoanBook(user, book);
        libraryService.addLoanBook(user, book2);
        // when
        Mockito.when(loanRepository.findLoanByUserId(id))
                .thenReturn(List.of(new Loan(user, book, LocalDate.now()), new Loan(user, book2, LocalDate.now())));
        List<Loan> loans = libraryService.getLoansByUserId(id);
        Mockito.when(loan.getUser()).thenReturn(user);
        Mockito.when(loan.getBook()).thenReturn(book);
        // then
        assertEquals(2, loans.size());
        assertEquals(id, loan.getUser().getId());
        assertTrue(book.isLoaned());
        assertTrue(book2.isLoaned());
        assertEquals("Aprendiendo Java", loan.getBook().getTitle());

    }

    @Test
    void testGetLoansByUserIdWhenUserDoesNotExist() throws NotFoundException {
        // given
        User user = new User("DevSenior", "1");
        Book book = new Book("Aprendiendo Java", "1", "DevSenior");
        libraryService.addBook(book);
        libraryService.addLoanBook(user, book);
        String id = "no exisitng id";
        // when
        Mockito.when(loanRepository.findLoanByUserId(id)).thenReturn(List.of(new Loan(user, book, LocalDate.now())));
        // then
        assertThrows(NotFoundException.class, () -> libraryService.getLoansByUserId(id));
    }

    @Test
    void testGetLoansByUserIdWhenUserHasNoLoans() throws NotFoundException {
        // given
        String name = "DevSenior";
        String id = "1";
        libraryService.addUsers(name, id);
        // when-then
        assertThrows(NotFoundException.class, () -> libraryService.getLoansByUserId(id));
    }

    @Test
    void testGetUsersById() throws NotFoundException {
        // given
        String name = "DevSenior";
        String id = "1";
        User user = new User(name, id);
        libraryService.addUsers(name, id);
        // when

        libraryService.getUsersById(id);
        // then
        assertNotNull(user);
        assertEquals(id, user.getId());

    }

    @Test
    void testGetUsersByIdWhenUserDoesNotExist() throws NotFoundException {
        // given
        String id = "1";
        String name = "DevSenior";
        libraryService.addUsers(name, id);
        String wrongId = "2";

        // when-then
        assertThrows(NotFoundException.class, () -> libraryService.getUsersById(wrongId));
    }

    @Test
    void testReturnBook() throws NotFoundException {
        // given
        Book book = new Book("Aprendiendo Java", "1", "DevSenior");
        libraryService.addBook(book);
        User user = new User("DevSenior", "1");
        libraryService.addUsers(user.getName(), user.getId());
        libraryService.addLoanBook(user, book);
        // when
        libraryService.returnBook(book);
        // then
        assertFalse(book.isLoaned());
    }

    
    @Test
    void testReturnBookWhenBookIsNotLoaned() throws NotFoundException {
        // given
        Book book = new Book("Aprendiendo Java", "1", "DevSenior");
        libraryService.addBook(book);
        // when-then
        assertThrows(NotFoundException.class, () -> libraryService.returnBook(book));
    }

}
