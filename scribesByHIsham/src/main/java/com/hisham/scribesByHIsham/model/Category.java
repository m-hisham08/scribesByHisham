package com.hisham.scribesByHIsham.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Enumerated(value = EnumType.STRING)
    private CategoryName name;

    @ManyToMany(mappedBy = "categories", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
    private Set<Article> articles = new HashSet<>();

    public void addArticle(Article article){
        articles.add(article);
        article.getCategories().add(this);
    }

    public void removeArticle(Article article){
        articles.remove(article);
        article.getCategories().remove(this);
    }
}
