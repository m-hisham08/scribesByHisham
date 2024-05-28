package com.hisham.scribesByHIsham.controller;

import com.hisham.scribesByHIsham.exception.ResourceNotFoundException;
import com.hisham.scribesByHIsham.model.Comment;
import com.hisham.scribesByHIsham.model.User;
import com.hisham.scribesByHIsham.model.UserIdentityAvailability;
import com.hisham.scribesByHIsham.payload.CommentResponse;
import com.hisham.scribesByHIsham.payload.PagedResponse;
import com.hisham.scribesByHIsham.payload.UserProfile;
import com.hisham.scribesByHIsham.payload.UserSummary;
import com.hisham.scribesByHIsham.repository.ArticleRepository;
import com.hisham.scribesByHIsham.repository.CommentRepository;
import com.hisham.scribesByHIsham.repository.LikeRepository;
import com.hisham.scribesByHIsham.repository.UserRepository;
import com.hisham.scribesByHIsham.security.CurrentUser;
import com.hisham.scribesByHIsham.security.UserPrincipal;
import com.hisham.scribesByHIsham.utils.AppConstants;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/user/me")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return new UserSummary(userPrincipal.getId(), userPrincipal.getUsername(), userPrincipal.getFirstName(), userPrincipal.getLastName());
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        int likeCount = likeRepository.countByUser(user);
        int commentCount = commentRepository.countByUser(user);

        return new UserProfile(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getCreatedAt(), likeCount, commentCount);
    }

    @GetMapping("user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username){
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email){
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @Transactional
    @GetMapping("user/{username}/comments")
    public PagedResponse<CommentResponse> getUserComments(
            @PathVariable(value = "username") String username,
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size
    ){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        UserSummary userSummary = new UserSummary(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName());

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdBy");
        Page<Comment> pagedComments = commentRepository.findByUser(user, pageable);

        List<CommentResponse> commentResponses = new ArrayList<>();
        for(Comment comment: pagedComments){
            commentResponses.add(new CommentResponse(comment.getId(), comment.getContent(), userSummary, comment.getCreatedAt()));
        }

        return new PagedResponse<CommentResponse>(commentResponses, pagedComments.getNumber(), pagedComments.getSize(), pagedComments.getNumberOfElements(), pagedComments.getTotalPages(), pagedComments.isLast());
    }
}
