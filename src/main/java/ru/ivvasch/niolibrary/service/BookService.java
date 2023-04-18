package ru.ivvasch.niolibrary.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ivvasch.niolibrary.model.Book;
import ru.ivvasch.niolibrary.repository.BookRepository;

import java.io.BufferedInputStream;
import java.io.IOException;

@Service
public class BookService {
    private final BookRepository bookRepository;


    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book findBookById(int id) {
        Book bookById = bookRepository.findBookById(id);
        return bookById;
    }

    public Book save(MultipartFile file, String dataOfBook) {
        ObjectMapper mapper = new ObjectMapper();
        Book book;
        try {
            book = mapper.readValue(dataOfBook, Book.class);
            book.setData(new BufferedInputStream(file.getInputStream()).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bookRepository.save(book);
    }
}
