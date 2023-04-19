package ru.ivvasch.niolibrary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ivvasch.niolibrary.model.Book;
import ru.ivvasch.niolibrary.repository.BookRepository;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class BookService {
    private final BookRepository bookRepository;


    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public File findBookById(int id) {
        Book bookById = bookRepository.findBookById(id);
        File targetFile;
        try {
            targetFile = new File(bookById.getName());
            FileOutputStream fileOutputStream = FileUtils.openOutputStream(targetFile);
            fileOutputStream.write(bookById.getData());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return targetFile;
    }

    public Book save(MultipartFile file, String dataOfBook) {
        ObjectMapper mapper = new ObjectMapper();
        Book book;
        if (dataOfBook != null) {
            try {
                book = mapper.readValue(dataOfBook, Book.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            book = new Book();
            String originalFilename = file.getOriginalFilename();
            book.setName(originalFilename);
        }
        try {
            book.setData(new BufferedInputStream(file.getInputStream()).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bookRepository.save(book);
    }
}
