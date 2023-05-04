package ru.ivvasch.niolibrary.controller;

import io.minio.GetObjectResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ivvasch.niolibrary.model.Book;
import ru.ivvasch.niolibrary.model.Type;
import ru.ivvasch.niolibrary.service.BookService;
import ru.ivvasch.niolibrary.service.MinioService;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/library")
public class ReadWriteController {
    private final BookService bookService;
    private final MinioService minioService;

    public ReadWriteController(BookService bookService, MinioService minioService) {
        this.bookService = bookService;
        this.minioService = minioService;
    }

    @GetMapping
    public String mainLibraryPage() {
        return "library";
    }

    @ModelAttribute
    public void addBookFromDB(Model model) {
        List<Book> books = bookService.findAll();
        Type[] types = Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(books, type));
        }
    }

    @ModelAttribute
    public Book book() {
        return new Book();
    }


    private Iterable<Book> filterByType(List<Book> books, Type type) {
        return books.stream()
                .filter(x -> x.getType().equals(type))
                .toList();
    }

    @PostMapping
    public String saveBook(@RequestParam("file") MultipartFile file, String type, String name) {
        bookService.save(file, type, name);
        minioService.saveBookToMinio(file, type);
        return "redirect:/library";
    }

    @GetMapping("/upload")
    public String addBook() {
        return "addbook";
    }

    @GetMapping("{fileName}")
    public String checkAndDownload(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileName") String fileName){
        if (!minioService.isExistBook(fileName)) {
            return "notfound";
        }
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        String typeOfBook = bookService.getTypeOfBook(fileName);
        GetObjectResponse objectResponse = minioService.getBookFromMinio(fileName, typeOfBook);
        try {
            IOUtils.copy(objectResponse, response.getOutputStream());
            response.flushBuffer();
            objectResponse.close();
        } catch (IOException e) {
            throw new RuntimeException("Something wrong when we get file");
        }
        return null;
}
