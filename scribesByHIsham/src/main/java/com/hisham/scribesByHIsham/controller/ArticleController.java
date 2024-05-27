package com.hisham.scribesByHIsham.controller;

import com.hisham.scribesByHIsham.exception.BadRequestException;
import com.hisham.scribesByHIsham.exception.ResourceNotFoundException;
import com.hisham.scribesByHIsham.exception.UnauthorizedException;
import com.hisham.scribesByHIsham.model.Article;
import com.hisham.scribesByHIsham.model.Category;
import com.hisham.scribesByHIsham.model.CategoryName;
import com.hisham.scribesByHIsham.model.User;
import com.hisham.scribesByHIsham.payload.*;
import com.hisham.scribesByHIsham.repository.ArticleRepository;
import com.hisham.scribesByHIsham.repository.CategoryRepository;
import com.hisham.scribesByHIsham.repository.LikeRepository;
import com.hisham.scribesByHIsham.repository.UserRepository;
import com.hisham.scribesByHIsham.security.CurrentUser;
import com.hisham.scribesByHIsham.security.UserPrincipal;
import com.hisham.scribesByHIsham.utils.AppConstants;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @GetMapping("")
    public PagedResponse<ArticleSummaryResponse> getAllArticles(@CurrentUser UserPrincipal userPrincipal,
                                                 @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                 @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size
                                                 ){
        if(page < 0){
            throw new BadRequestException("Page number cannot be less than zero.");
        }
        if(size > AppConstants.MAX_PAGE_SIZE){
            throw new BadRequestException("Page number cannot exceed " + AppConstants.MAX_PAGE_SIZE);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdBy");
        Page<Article> pagedArticles = articleRepository.findAll(pageable);
        List<Article> articles = pagedArticles.getContent();

        List<ArticleSummaryResponse> articleSummaryResponses = new ArrayList<>();
        for(Article article : articles){
            User user = userRepository.findById(article.getCreatedBy())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "userId", article.getCreatedBy()));
            UserSummary userSummary = new UserSummary(
                    user.getId(), user.getUsername(), user.getFirstName(), user.getLastName()
            );

            List<CategoryResponse> categories = new ArrayList<>();
            for (Category category: article.getCategories()){
                categories.add(new CategoryResponse(category.getName()));
            }

            articleSummaryResponses.add(
                    new ArticleSummaryResponse(article.getId(), article.getHeading(), categories, userSummary)
            );
        }

        return new PagedResponse<ArticleSummaryResponse>(
                articleSummaryResponses, pagedArticles.getNumber(), pagedArticles.getSize(), (int) pagedArticles.getTotalElements(), pagedArticles.getTotalPages(),pagedArticles.isLast());
    }

    @PostMapping("")
    public ResponseEntity<?> createArticle(@CurrentUser UserPrincipal userPrincipal,
                                        @RequestBody @Valid ArticleRequest articleRequest){
        Article article = new Article();
        article.setHeading(articleRequest.getHeading());
        article.setContent(articleRequest.getContent());
        article.setCreatedBy(userPrincipal.getId());
        Set<Category> categories = new HashSet<>();
        for (CategoryName categoryName : articleRequest.getCategories()) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            categories.add(category);
        }
        article.setCategories(categories);

        Article peristedArticle = articleRepository.save(article);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{articleId}")
                .buildAndExpand(peristedArticle.getId())
                .toUri();

        logger.info(String.valueOf(location));

        return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "New article created at " + location));
    }

    @GetMapping("/{articleId}")
    public ArticleResponse getSingleArticle(@CurrentUser UserPrincipal userPrincipal, @PathVariable Long articleId){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "articleId", articleId));

        User user = userRepository.findById(article.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", article.getCreatedBy()));
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

    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> deleteArticle(@CurrentUser UserPrincipal userPrincipal,
                                           @PathVariable Long articleId)
    {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "articleId", articleId));

        if(!userPrincipal.getId().equals(article.getCreatedBy())){
            throw new UnauthorizedException("You do not have permission to delete this article!");
        }

        articleRepository.delete(article);

        return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, "Article deleted successfully!"));
    }
}

