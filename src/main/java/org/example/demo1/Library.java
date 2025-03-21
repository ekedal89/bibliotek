package org.example.demo1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> books;
    private static final String FILE_NAME = "books.dat";
    private List<Book> availableBooks = new ArrayList<>();
    private List<Book> borrowedBooks = new ArrayList<>();

    public Library() {
        this.books = loadBooks();
    }

    public void addBook(String title, String author) {
        if (title == null || author == null) {
            throw new IllegalArgumentException("Title and author are required");
        }
        Book newBook = new Book(title, author);
        books.add(newBook);
        saveBooks();
    }

    public void borrowBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book is required");
        }
        if (book.isBorrowed()) {
            throw new IllegalArgumentException("Book is already borrowed");
        }
        book.borrow();
        saveBooks();
    }

    public void returnBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book is required");
        }
        if (!book.isBorrowed()) {
            throw new IllegalArgumentException("Book is not borrowed");
        }
        book.returnBook();
        saveBooks();
    }

    public void removeBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book must be selected.");
        }
        books.remove(book);
        availableBooks.remove(book);
        borrowedBooks.remove(book);
        saveBooks();
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public List<Book> getAvailableBooks() {
        availableBooks.clear();
        for (Book book : books) {
            if (!book.isBorrowed()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    public List<Book> getBorrowedBooks() {
        borrowedBooks.clear();
        for (Book book : books) {
            if (book.isBorrowed()) {
                borrowedBooks.add(book);
            }
        }
        return borrowedBooks;
    }

    private void saveBooks() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(books);
        } catch (IOException e) {
            System.err.println("Could not save books: " + e.getMessage());
        }
    }

    private List<Book> loadBooks() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Book>) in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            System.err.println("Could not load books: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

