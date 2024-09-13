package com.kodbook.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kodbook.entities.Post;
import com.kodbook.services.PostService;

@Controller
public class PostController {
	@Autowired
	PostService service;
	
	@PostMapping("/createPost")
	public String createPost(@RequestParam ("caption") String caption,
            @RequestParam("photo") MultipartFile photo	) {
		Post post = new Post();
		post.setCaption(caption);
		try {						
			post.setPhoto(photo.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		service.createPost(post);
		return "home";
	}
	
	@GetMapping("/likePost")
	public String likePost(@RequestParam Long id, Model model) {
		Post post= service.getPost(id);
		post.setLikes(post.getLikes() + 1);
		service.updatePost(post);
		
		List<Post> allPosts = service.fetchAllPosts();
		model.addAttribute("allPosts", allPosts);
		return "home";
	}
	
	@GetMapping("/addComment")
	public String addComment(@RequestParam Long id, 
			@RequestParam String comment, Model model) {
		Post post= service.getPost(id);
		post.getComments().add(comment);
		service.updatePost(post);
		
		List<Post> allPosts = service.fetchAllPosts();
		model.addAttribute("allPosts", allPosts);
		return "home";
	}
}
