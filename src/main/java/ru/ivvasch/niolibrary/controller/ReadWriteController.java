package ru.ivvasch.niolibrary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ivvasch.niolibrary.model.Book;
import ru.ivvasch.niolibrary.service.BookService;

import java.io.File;


@RestController
@RequestMapping("/book")
public class ReadWriteController {
    private final BookService bookService;

    public ReadWriteController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping()
    public ResponseEntity<Book> saveBook(@RequestParam("file") MultipartFile file, String book) {
        Book savedBook = bookService.save(file, book);
        return ResponseEntity.ok(savedBook);
    }

    @GetMapping("{id}")
    public File getBookById(@PathVariable int id) {
        File bookById = bookService.findBookById(id);
        return bookById;
    }
}
