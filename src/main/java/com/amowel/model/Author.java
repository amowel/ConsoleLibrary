package com.amowel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by amowel on 28.04.17.
 */
@Entity
@Data
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author" , orphanRemoval = true ,fetch = FetchType.EAGER)
    private List<Book> books;
    @Column(unique = true)
    private String name;

    public String toString() {
        return "com.amowel.model.Author(id=" + this.getId() + ", name=" + this.getName() + ")";
    }
    public String getInfo(){
        return new StringBuilder("Author: ")
                .append(name)
                .append(" Books: ")
                .append(books.stream().map(Book::getTitle).reduce((s, s2) -> String.join(", ",s,s2)).orElse("Has no books"))
                .toString();
    }
}
