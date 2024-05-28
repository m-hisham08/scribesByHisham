package com.hisham.scribesByHIsham.controller;

import com.hisham.scribesByHIsham.exception.ResourceNotFoundException;
import com.hisham.scribesByHIsham.exception.UnauthorizedException;
import com.hisham.scribesByHIsham.model.Article;
import com.hisham.scribesByHIsham.model.Comment;
import com.hisham.scribesByHIsham.model.User;
import com.hisham.scribesByHIsham.payload.*;
import com.hisham.scribesByHIsham.repository.ArticleRepository;
import com.hisham.scribesByHIsham.repository.CommentRepository;
import com.hisham.scribesByHIsham.repository.UserRepository;
import com.hisham.scribesByHIsham.security.CurrentUser;
import com.hisham.scribesByHIsham.security.UserPrincipal;
import com.hisham.scribesByHIsham.utils.AppConstants;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/articles/{articleId}")
public class CommentController {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/comments")
    @Transactional
    public PagedResponse<CommentResponse> getAllComments(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long articleId,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size
            ){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "articleId", articleId));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdBy");
        Page<Comment> pagedComment = commentRepository.findByArticle(article, pageable);

        List<Comment> comments = pagedComment.getContent();

        List<CommentResponse> commentResponses = new ArrayList<>();
        for(Comment comment: comments){
            User user = userRepository.findById(comment.getCreatedBy())
                            .orElseThrow(() -> new ResourceNotFoundException("User", "userId", comment.getCreatedBy()));
            UserSummary userSummary = new UserSummary(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName());
            commentResponses.add(new CommentResponse(comment.getId(), comment.getContent(), userSummary, comment.getCreatedAt()));
        }
        return new PagedResponse<CommentResponse>(commentResponses, pagedComment.getNumber(), pagedComment.getSize(), (int) pagedComment.getTotalElements(), pagedComment.getTotalPages(), pagedComment.isLast());
    }

    @GetMapping("/comments/{commentId}")
    @Transactional
    public CommentResponse getSingleComments(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long articleId,
            @PathVariable Long commentId
    ){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "articleId", articleId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "commentId", commentId));


        UserSummary userSummary = new UserSummary(
                comment.getUser().getId(), comment.getUser().getUsername(), comment.getUser().getFirstName(), comment.getUser().getLastName()
        );

        return new CommentResponse(comment.getId(), comment.getContent(), userSummary, comment.getCreatedAt());
    }

    @DeleteMapping("/comments/{commentId}")
    @Transactional
    public ResponseEntity<?> deleteSingleComments(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long articleId,
            @PathVariable Long commentId
    ){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "articleId", articleId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "commentId", commentId));

        if(comment.getCreatedBy() == userPrincipal.getId()){
            throw new UnauthorizedException("You do not have the permission to perform this action!");
        }

        commentRepository.delete(comment);

        return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, "Comment deleted successfully!"));
    }

    @PostMapping("/comments")
    @Transactional
    public ResponseEntity<?> createComment(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable Long articleId,
            @RequestBody @Valid CommentRequest commentRequest
            ){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "articleId", articleId));
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userPrincipal.getId()));
        Comment comment = new Comment(user, article, commentRequest.getContent());
        article.addComment(comment);
        Article persistedArticle = articleRepository.save(article);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{commentId}")
                .buildAndExpand(comment.getId())
                .toUri();

        return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "Comment posted successfully!"));
    }
}
