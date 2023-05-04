package ru.ivvasch.niolibrary.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ivvasch.niolibrary.model.Book;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {

    Book save(Book book);

    Book findBookById(Integer id);

    @Query("select * from book")
    List<Book> findAll();

    Book findBookByFileName(String filename);
}
