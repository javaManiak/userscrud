package com.example.userscrud.controller;

import com.example.userscrud.entity.Post;
import com.example.userscrud.entity.User;
import com.example.userscrud.service.PostService;
import com.example.userscrud.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private PostService postService;

    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{email}")
    public User retrieveUser(@PathVariable String email) {
        return userService.getUser(email);
    }

    @GetMapping("/userName/{name}")
    public List<User> retrieveUserByName(@PathVariable String name) {
        return userService.getUserByName(name);
    }

    @DeleteMapping("/{email}")
    public void deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
    }

    @DeleteMapping("/userName/{name}")
    public void deleteByName(@PathVariable String name) {
        userService.deleteByName(name);
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userService.createUser(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{email}")
                .buildAndExpand(savedUser.getEmail()).toUri();
        // returning URI

        return ResponseEntity.created(location).build();
    }

    // To retrieve posts of User
    @GetMapping("/{email}/posts")
    public List<Post> retrievePosts(@PathVariable String email) {
        User user = userService.getUser(email);
        return user.getPosts();
    }

    @PostMapping("/{email}/posts")
    public ResponseEntity<Post> createPost(@RequestBody Post post, @PathVariable String email) {
        User postuser = userService.getUser(email);
        post.setUser(postuser);

        Post savedPost = postService.createPost(post);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("")
                .buildAndExpand(savedPost.getId()).toUri();
        return ResponseEntity.created(location).build();
    }


}
