package com.decagon.fitnessoapp.model.blog;

import com.decagon.fitnessoapp.model.user.Author;
import com.decagon.fitnessoapp.model.user.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false)
    private Person person;

    @NotNull
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    private String content;

    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private Author author;
}
