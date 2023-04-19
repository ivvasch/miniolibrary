package ru.ivvasch.niolibrary.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ivvasch.niolibrary.model.Book;
import ru.ivvasch.niolibrary.model.Type;
import ru.ivvasch.niolibrary.repository.BookRepository;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;


    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book findBookById(int id) {
        return bookRepository.findBookById(id);
    }


    public Book save(MultipartFile file, String type, String name) {
        Book book = new Book();
        book.setType(Type.valueOf(type));
        book.setName(name);
        String originalFilename = file.getOriginalFilename();
        book.setFileName(originalFilename);
        return bookRepository.save(book);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public String getTypeOfBook(String fileName) {
        Book bookByFileName = bookRepository.findBookByFileName(fileName);
        return bookByFileName.getType().toString();
    }
}
