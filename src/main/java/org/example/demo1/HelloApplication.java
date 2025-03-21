package org.example.demo1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private ComboBox<Book> bookComboBox;
    private ComboBox<Book> borrowedBookBox;
    private ComboBox<Book> deleteBookComboBox;
    private Label messageLabel;
    private ObservableList<Book> books;
    private ObservableList<Book> borrowedBooks;
    private Library library;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {

        library = new Library();
        books = FXCollections.observableArrayList(library.getAvailableBooks());
        borrowedBooks = FXCollections.observableArrayList(library.getBorrowedBooks());

        bookComboBox = new ComboBox<>(books);
        borrowedBookBox = new ComboBox<>(borrowedBooks);
        deleteBookComboBox = new ComboBox<>(books);

        Label borrowLabel = new Label("Choose book to borrow");
        borrowLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label returnLabel = new Label("Choose book to return");
        returnLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label deleteLabel = new Label("Choose book to delete");
        deleteLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Button borrowButton = new Button("Borrow book");
        borrowButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        borrowButton.setOnAction(e -> borrowBook());

        Button returnButton = new Button("Return book");
        returnButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        returnButton.setOnAction(e -> returnBook());

        Button addBookButton = new Button("Add book");
        addBookButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-font-weight: bold;");
        addBookButton.setOnAction(e -> addBook());

        Button deleteBookButton = new Button("Delete book");
        deleteBookButton.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-font-weight: bold;");
        deleteBookButton.setOnAction(e -> deleteBook());

        messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        messageLabel.setTextFill(Color.DARKBLUE);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(15);
        gridPane.setHgap(15);
        gridPane.add(addBookButton, 1, 0);
        gridPane.add(borrowLabel, 0, 1);
        gridPane.add(bookComboBox, 0, 2);
        gridPane.add(borrowButton, 1, 2);
        gridPane.add(returnLabel, 0, 3);
        gridPane.add(borrowedBookBox, 0, 4);
        gridPane.add(returnButton, 1, 4);
        gridPane.add(deleteLabel, 0, 5);
        gridPane.add(deleteBookComboBox, 0, 6);
        gridPane.add(deleteBookButton, 1, 6);
        gridPane.add(messageLabel, 0, 7, 2, 1);


        Scene scene = new Scene(gridPane, 650, 650);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

    private void borrowBook() {
        Book selectedBook = bookComboBox.getValue();
        if (selectedBook != null) {
            library.borrowBook(selectedBook);
            updateBookLists();
            messageLabel.setText("You borrowed: " + selectedBook.getTitle());
        } else {
            messageLabel.setText("Please select a book to borrow.");
        }
    }

    private void returnBook() {
        Book selectedBook = borrowedBookBox.getValue();
        if (selectedBook != null) {
            library.returnBook(selectedBook);
            updateBookLists();
            messageLabel.setText("You returned: " + selectedBook.getTitle());
        } else {
            messageLabel.setText("Please select a book to return.");
        }
    }

    private void addBook() {
        TextInputDialog titleDialog = new TextInputDialog();
        titleDialog.setTitle("Add Book");
        titleDialog.setHeaderText("Enter the title of the new book");
        titleDialog.setContentText("Book title:");

        titleDialog.showAndWait().ifPresent(bookTitle -> {
            if (!bookTitle.trim().isEmpty()) {
                TextInputDialog authorDialog = new TextInputDialog();
                authorDialog.setTitle("Add Author");
                authorDialog.setHeaderText("Enter the author of the book");
                authorDialog.setContentText("Author name:");

                authorDialog.showAndWait().ifPresent(bookAuthor -> {
                    if (!bookAuthor.trim().isEmpty()) {
                        library.addBook(bookTitle, bookAuthor);
                        updateBookLists();
                        messageLabel.setText("Added book: " + bookTitle + " by " + bookAuthor);
                    } else {
                        messageLabel.setText("Please enter a valid author name.");
                    }
                });
            } else {
                messageLabel.setText("Please enter a valid book title.");
            }
        });
    }


    private void deleteBook() {
        Book selectedBook = deleteBookComboBox.getValue();
        if (selectedBook != null) {
            library.removeBook(selectedBook);
            updateBookLists();
            messageLabel.setText("Deleted book: " + selectedBook.getTitle());
        } else {
            messageLabel.setText("Please select a book to delete.");
        }
    }

    private void updateBookLists() {
        books.setAll(library.getAvailableBooks());
        borrowedBooks.setAll(library.getBorrowedBooks());
        bookComboBox.setItems(books);
        borrowedBookBox.setItems(borrowedBooks);
        deleteBookComboBox.setItems(books);
    }
}