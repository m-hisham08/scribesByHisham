package com.hisham.scribesByHIsham.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hisham.scribesByHIsham.model.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "articles")
public class Article extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 20, max = 100)
    private String heading;

    @Lob
    @NotBlank
    @Size(min = 50)
    private String content;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "article_categories",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonManagedReference
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "article", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "article", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    public void addCategory(Category category) {
        categories.add(category);
        category.getArticles().add(this);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
        category.getArticles().remove(this);
    }

    public void addLike(Like like){
        likes.add(like);
        like.setArticle(this);
    }

    public void removeLike(Like like){
        likes.remove(like);
        like.setArticle(null);
    }

    public void addComment(Comment comment){
        comments.add(comment);
        comment.setArticle(this);
    }

    public void removeComment(Comment comment){
        comments.remove(comment);
        comment.setArticle(null);
    }
}
