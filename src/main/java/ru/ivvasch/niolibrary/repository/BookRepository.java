package ru.ivvasch.niolibrary.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.ivvasch.niolibrary.model.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {

    Book save(Book book);

    Book findBookById(Integer id);

}
