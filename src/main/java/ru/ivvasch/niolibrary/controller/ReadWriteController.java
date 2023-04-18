package ru.ivvasch.niolibrary.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ivvasch.niolibrary.model.Book;
import ru.ivvasch.niolibrary.service.BookService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@RestController
@RequestMapping("/book")
public class ReadWriteController {
    private final BookService bookService;

    public ReadWriteController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping()
    public ResponseEntity<Book> saveBook(@RequestParam("file") MultipartFile file, String book) {
        // логика сохранения книги
        Book savedBook = bookService.save(file, book);
        return ResponseEntity.ok(savedBook);
    }

    @GetMapping("{id}")
    public File getBookById(@PathVariable int id) {
        // логика получения книги
        Book bookById = bookService.findBookById(id);
        File targetFile;
        try {
            targetFile = new File("test.doc");
            FileOutputStream fileOutputStream = FileUtils.openOutputStream(targetFile);
            fileOutputStream.write(bookById.getData());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return targetFile;
    }
}
