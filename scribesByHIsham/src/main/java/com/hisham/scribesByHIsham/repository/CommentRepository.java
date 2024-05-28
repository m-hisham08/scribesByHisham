package com.hisham.scribesByHIsham.repository;

import com.hisham.scribesByHIsham.model.Article;
import com.hisham.scribesByHIsham.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByArticle(Article article, Pageable pageable);
}
