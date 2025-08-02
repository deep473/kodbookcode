// src/main/java/com/kodbook/controllers/PostController.java
package com.kodbook.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kodbook.entities.Post;
import com.kodbook.entities.Post.PostType;
import com.kodbook.entities.User;
import com.kodbook.services.PostService;
import com.kodbook.services.UserService;

@Controller
public class PostController {

    @Autowired
    private PostService service;

    @Autowired
    private UserService userService;

    @PostMapping("/createPost")
    public String createPost(
            @RequestParam("caption") String caption,
            @RequestParam("postType") PostType postType,
            @RequestParam(value = "photo",    required = false) MultipartFile photo,
            @RequestParam(value = "videoUrl", required = false) String videoUrl,
            Model model,
            HttpSession session) {

        String username = (String) session.getAttribute("username");
        User user = userService.getUser(username);

        Post post = new Post();
        post.setUser(user);
        post.setCaption(caption);
        post.setPostType(postType);

        // branch based on type
        if (postType == PostType.IMAGE && photo != null && !photo.isEmpty()) {
            try {
                post.setPhoto(photo.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (postType == PostType.VIDEO) {
            post.setVideoUrl(videoUrl);
        }

        // persist
        service.createPost(post);

        // attach to user
        List<Post> posts = user.getPosts();
        if (posts == null) {
            posts = new ArrayList<>();
        }
        posts.add(post);
        user.setPosts(posts);
        userService.updateUser(user);

        // refresh feed
        List<Post> allPosts = service.fetchAllPosts();
        model.addAttribute("allPosts", allPosts);
        return "home";
    }

	
	@PostMapping("/likePost")
	public String likePost(@RequestParam Long id, Model model) {
		Post post= service.getPost(id);
		post.setLikes(post.getLikes() + 1);
		service.updatePost(post);
		
		List<Post> allPosts = service.fetchAllPosts();
		model.addAttribute("allPosts", allPosts);
		return "home";
	}
	
	@PostMapping("/deletePost")
	public String deletePost(@RequestParam Long id, Model model) {
		service.deletePost(id);
		
		List<Post> allPosts = service.fetchAllPosts();
		model.addAttribute("allPosts", allPosts);
		return "home";
	}
	
	@PostMapping("/addComment")
	public String addComment(@RequestParam Long id, 
			@RequestParam String comment, Model model) {
		System.out.println(comment);
		Post post= service.getPost(id);
		List<String> comments = post.getComments();
		if(comments == null) {
			comments = new ArrayList<String>();
		}
		comments.add(comment);
		post.setComments(comments);
		service.updatePost(post);
		
		List<Post> allPosts = service.fetchAllPosts();
		model.addAttribute("allPosts", allPosts);
		return "home";
	}
}
