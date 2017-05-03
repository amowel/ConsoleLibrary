package com.amowel.command;

import com.amowel.dal.AuthorDao;
import com.amowel.dal.BookDao;
import com.amowel.model.Author;
import com.amowel.model.Book;
import com.google.common.primitives.Ints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by amowel on 30.04.17.
 */
@Component
public class Commands implements CommandMarker {

    public static Scanner sc = new Scanner(System.in);
    @Autowired
    private AuthorDao authors;
    @Autowired
    private BookDao books;

    @CliCommand(value = {"add book"}, help = "Add a book to a library. Example: add book --a test --t test")
    public String addBook(
            @CliOption(key = "", help = "All information about book together. For example: add book Henal Ihor \"Harry Potter\"") String description,
            @CliOption(key = {"t", "title"}, help = "Specify title of a book") String title,
            @CliOption(key = {"a", "author"}, help = "Specify author of a book", unspecifiedDefaultValue = "UNKNOWN") String author
    ) {
        Author temp;
        String[] parameters;
        if (description == null) {
            if (title != null)
                parameters = new String[]{author, title};
            else
                return "Bad Syntax";
        } else
            parameters = description.split("\\s*\"");
        if (parameters.length != 2)
            return "Bad Syntax";
        if (authors.findByName(parameters[0]) == null) {
            temp = Author
                    .builder()
                    .name(parameters[0])
                    .build();
        } else
            temp = authors.findByName(parameters[0]);
        Book book = Book.builder().added(LocalDate.now()).author(temp).title(parameters[1]).build();
        if (books.findByTitle(book.getTitle()).stream().anyMatch(
                (e) -> e.getAuthor().getName().equals(book.getAuthor().getName())))
            return "The equal book is already exist";
        books.saveOrUpdate(book);
        return "OK";
    }

    @CliCommand(value = {"add author"}, help = "add new author.Example:\"add author George Martin\"")
    public String createAuthor(@CliOption(key = "", mandatory = true, help = "Specify the name of author") String name) {
        if (authors.findByName(name) != null)
            return "Author already exist";
        return "Author " + authors.saveOrUpdate(Author.builder().name(name).build()).getName() + " was successfully created";
    }

    @CliCommand(value = {"update book"}, help = "change information about book. Example: update book Harry Potter --t 1984 --a Oruell")
    public String updateBook(
            @CliOption(key = "", mandatory = true, help = "Title of the book, which you want to update.") String title,
            @CliOption(key = {"t", "title"}, help = "New title") String newTitle,
            @CliOption(key = {"a", "author"}, help = "New author") String author
    ) {
        List<Book> bookList = books.findByTitle(title);
        Book book;
        if (bookList == null || bookList.isEmpty())
            return "There is no book with this value";
        if(bookList.size()>1) {
            book = chooseBook(bookList, "Please choose which book do you want to update");
        }
        else
            book = bookList.get(0);
        if (newTitle != null)
            book.setTitle(newTitle);
        if (author != null) {
            Author newAuthor = authors.findByName(author);
            if (newAuthor == null) {
                newAuthor = Author.builder().name(author).build();
            }
            book.setAuthor(newAuthor);
        }
        if (books.findByTitle(book.getTitle()).stream().anyMatch(
                (e) -> e.getAuthor().getName().equals(book.getAuthor().getName())))
            return "The equal book is already exist";
        books.saveOrUpdate(book);
        return "OK";
    }

    @CliCommand(value = {"update author"}, help = "update information about author. Example: update author Henal Ihor --n Ransom Riggs")
    public String updateAuthor(
            @CliOption(key = "", mandatory = true, help = "Name of an author, whose information you want to update") String name,
            @CliOption(key = {"n", "name"}, mandatory = true, help = "New name") String newName
    ) {
        Author author = authors.findByName(name);
        if (author == null)
            return "There is no author with this name";
        if (authors.findByName(newName) != null)
            return "Can't change author name, because name is already present";
        author.setName(newName);
        authors.saveOrUpdate(author);
        return "OK";
    }

    @CliCommand(value = {"get book"}, help = "get books from library. Example: get book")
    public void getBooks(
            @CliOption(key = {"c", "count"}, help = "Quantity of books, which should be displayed. Ignored when title flag presents") Long count,
            @CliOption(key = {"", "t", "title"}, help = "Title of books, which should be displayed") String title
    ) {
        if (title == null && count == null)
            books.findAll().stream().map(Book::getInfo).forEach(System.out::println);
        else if (title == null)
            books.findAll().stream().collect(toShuffledStream()).limit(count).map(Book::getInfo).forEach(System.out::println);
        else if (count == null) {
            if (books.findByTitle(title) == null) {
                System.out.println("No books with entered name");
                return;
            }
            books.findByTitle(title).stream().map(Book::getInfo).forEach(System.out::println);
        } else if (count <= 0)
            System.out.println("You entered unappropriated value");
        else {
            System.out.println("Count property is ignored, when title is specified");
            books.findAll().stream().map(Book::getInfo).forEach(System.out::println);
        }
    }

    @CliCommand(value = {"get author"}, help = "get authors. Example: get author --c 5")
    public void getAuthors(
            @CliOption(key = {"c", "count"}, help = "Quantity of authors, which should be displayed. Ignored when name flag presents") Long count,
            @CliOption(key = {"", "n", "name"}, help = "Name of an author, information about which should be displayed") String name
    ) {
        if (count == null && name == null)
            authors.findAll().stream().map(Author::getInfo).forEach(System.out::println);
        else if (name == null)
            authors.findAll().stream().collect(toShuffledStream()).map(Author::getInfo).limit(count).forEach(System.out::println);
        else if (count == null) {
            if (authors.findByName(name) == null) {
                System.out.println("No authors with entered name");
                return;
            }
            System.out.println(authors.findByName(name).getInfo());
        } else if (count < 0)
            System.out.println("You entered unappropriated value");
        else {
            System.out.println("Count property is ignored, when title is specified");
            authors.findByName(name).getInfo();
        }
    }

    @CliCommand(value = "remove author", help = "Remove author. Example: remove author Henal Ihor")
    public String removeAuthor(
            @CliOption(key = "", mandatory = true, help = "Name of an author, who should be removed") String name
    ) {
        Author author = authors.findByName(name);
        if (author == null)
            return "There is no author with this name";
        authors.delete(author);
        return "OK";
    }

    @CliCommand(value = "remove book", help = "Remove book. Takes one parameter which indicates a title of a book. Example: remove book Harry Potter")
    public String removeBook(
            @CliOption(key = {"title", ""}, mandatory = true, help = "Title of a book, which should be removed") String title
    ) {
        List<Book> bookList = books.findByTitle(title);
        if (bookList == null || bookList.isEmpty())
            return "There is no book with this value";
        if(bookList.size()>1) {
            Book book = chooseBook(bookList, "Please choose which book do you want to remove");
        }
        books.delete(bookList.get(0));
        return "OK";
    }

    private Book chooseBook(List<Book> bookList, String firstMessage) {
        System.out.println(firstMessage);
        for (int i = 1; i <= bookList.size(); i++) {
            System.out.println(i + ". " + bookList.get(i - 1).getInfo());
        }
        System.out.print("> ");
        String number = sc.nextLine();
        System.out.println(number);
        while (number.isEmpty() || Ints.tryParse(number) == null || Ints.tryParse(number) <= 0 || Ints.tryParse(number) > bookList.size()) {
            System.out.println("You entered wrong input, please try again");
            System.out.print("> ");
            number = sc.nextLine();
        }
        return bookList.get(Ints.tryParse(number) - 1);
    }

    private <T> Collector<T, ?, Stream<T>> toShuffledStream() {
        return Collectors.collectingAndThen(Collectors.toList(), collected -> {
            Collections.shuffle(collected);
            return collected.stream();
        });
    }


}
