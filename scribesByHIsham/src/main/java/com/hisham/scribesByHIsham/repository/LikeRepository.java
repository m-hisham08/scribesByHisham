package com.hisham.scribesByHIsham.repository;

import com.hisham.scribesByHIsham.model.Article;
import com.hisham.scribesByHIsham.model.Like;
import com.hisham.scribesByHIsham.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Boolean existsByUserAndArticle(User user, Article article);
    Optional<Like> findByUserAndArticle(User user, Article article);
    void deleteByUserAndArticle(User user, Article article);
    int countByArticle(Article article);
    int countByUser(User user);
}
