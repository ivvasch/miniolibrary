package ru.ivvasch.niolibrary.service;

import org.springframework.web.multipart.MultipartFile;
import ru.ivvasch.niolibrary.model.Book;

import java.util.List;

public interface BookService {
    Book findBookById(int id);

    Book save(MultipartFile file, String type, String name);

    List<Book> findAll();

    String getTypeOfBook(String fileName);
}
