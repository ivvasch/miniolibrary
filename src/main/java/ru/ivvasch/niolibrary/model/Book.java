package ru.ivvasch.niolibrary.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Getter
@Setter
@EqualsAndHashCode
@Table("book")
public class Book {
    @Id
    private Integer id;
    private String name;
    @Column(value = "fileName")
    private String fileName;
    private Type type;

}
