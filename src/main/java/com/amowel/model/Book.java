package com.amowel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by amowel on 28.04.17.
 */
@Entity
@Data
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"title","author_id"})})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Author author;
    @Column(nullable = false)
    private String title;
    @Column()
    private LocalDate added;

    public String toString() {
        return "com.amowel.model.Book(id=" + this.getId() + ", author=" + this.getAuthor() + ", title=" + this.getTitle() + ", created=" + this.getAdded() + ")";
    }
    public String getInfo(){
        return new StringBuilder("Book: ")
                .append(title)
                .append("  ")
                .append("Author: ")
                .append(author.getName())
                .append("  ")
                .append("added: ")
                .append(added)
                .toString();
    }
}
