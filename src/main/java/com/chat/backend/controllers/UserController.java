package com.chat.backend.controllers;

import com.chat.backend.models.User;
import com.chat.backend.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public User getUserById(@RequestParam Long id) {
		return userService.findById(id);
	}

	@DeleteMapping
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteUserById(@RequestParam Long id) {
		userService.deleteUserById(id);
	}
}
