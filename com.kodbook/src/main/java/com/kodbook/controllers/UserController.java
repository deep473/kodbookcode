package com.kodbook.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kodbook.entities.Post;
import com.kodbook.entities.User;
import com.kodbook.services.PostService;
import com.kodbook.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	UserService service;
	@Autowired
	PostService postService;
	@PostMapping("/signUp")
	public String addUser(@ModelAttribute User user) {
		//user exists?
		String username = user.getUsername();
		String email = user.getEmail();
		boolean status = service.userExists(username, email);
		if(status == false) {
			service.addUser(user);
		}
		return "index";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam String username,
			@RequestParam String password,
			Model model, HttpSession session)	{
		boolean status = service.validateUser(username, password);
		if(status == true) {
			List<Post> allPosts = postService.fetchAllPosts();
			
			session.setAttribute("username", username);
			model.addAttribute("session", session);
			
			model.addAttribute("allPosts", allPosts);
			
			return "home";
		}
		else {
			return "index";
		}
	}
	
	@PostMapping("/updateProfile")
	public String updateProfile(@RequestParam String dob, @RequestParam String gender,
			@RequestParam String city, @RequestParam String bio,
			@RequestParam String college, @RequestParam String linkedIn,
			@RequestParam String gitHub, @RequestParam MultipartFile profilePic
			, HttpSession session,
			Model model) {
		
		String username = (String) session.getAttribute("username");
		
		//fetch user object using username
		User user = service.getUser(username);
		//update and save object
		user.setDob(dob);
		user.setGender(gender);
		user.setCity(city);
		user.setBio(bio);
		user.setCollege(college);
		user.setLinkedIn(linkedIn);
		user.setGitHub(gitHub);
		try {						
			user.setProfilePic(profilePic.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		service.updateUser(user);
		model.addAttribute("user", user);
		return "myProfile";
	}
}
