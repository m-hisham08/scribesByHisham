package com.hisham.scribesByHIsham.controller;

import com.hisham.scribesByHIsham.exception.ResourceNotFoundException;
import com.hisham.scribesByHIsham.model.Article;
import com.hisham.scribesByHIsham.model.Category;
import com.hisham.scribesByHIsham.model.Like;
import com.hisham.scribesByHIsham.model.User;
import com.hisham.scribesByHIsham.payload.ArticleResponse;
import com.hisham.scribesByHIsham.payload.CategoryResponse;
import com.hisham.scribesByHIsham.payload.UserSummary;
import com.hisham.scribesByHIsham.repository.ArticleRepository;
import com.hisham.scribesByHIsham.repository.LikeRepository;
import com.hisham.scribesByHIsham.repository.UserRepository;
import com.hisham.scribesByHIsham.security.CurrentUser;
import com.hisham.scribesByHIsham.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/like")
public class LikeController {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("")
    public ArticleResponse LikeOrUnlikeArticle(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "like") int like,
            @RequestParam(value = "articleId") Long articleId
            ){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "articleId", articleId));

        User user = userRepository.findById((userPrincipal.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userPrincipal.getId()));

        if(!likeRepository.existsByUserAndArticle(user, article) && like == 1){
            Like newLike = new Like();
            newLike.setUser(user);
            article.addLike(newLike);
            articleRepository.save(article);
        }
        else if(likeRepository.existsByUserAndArticle(user, article) && like == 0){
            Like existingLike = likeRepository.findByUserAndArticle(user, article)
                    .orElseThrow(() -> new ResourceNotFoundException("Like", "UserID and ArticleID", user.getId()));
            article.removeLike(existingLike);
            articleRepository.save(article);
        }

        UserSummary userSummary = new UserSummary(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName());

        List<CategoryResponse> categories = new ArrayList<>();
        for (Category category: article.getCategories()){
            categories.add(new CategoryResponse(category.getName()));
        }

        int likesCount = likeRepository.countByArticle(article);

        return new ArticleResponse(
                article.getId(), article.getHeading(), article.getContent(),categories,userSummary, likesCount
        );
    }
}
