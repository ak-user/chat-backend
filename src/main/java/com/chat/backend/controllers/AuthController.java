package com.chat.backend.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.chat.backend.models.ERole;
import com.chat.backend.models.Role;
import com.chat.backend.models.User;
import com.chat.backend.payload.request.LoginRequest;
import com.chat.backend.payload.request.SignupRequest;
import com.chat.backend.payload.response.JwtResponse;
import com.chat.backend.payload.response.MessageResponse;
import com.chat.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat.backend.repository.RoleRepository;
import com.chat.backend.repository.UserRepository;
import com.chat.backend.security.jwt.JwtUtils;
import com.chat.backend.security.services.UserDetailsImpl;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static final String ROLE_IS_NOT_FOUND_ERROR_MESSAGE = "Role is not found";

	final AuthenticationManager authenticationManager;
	final UserRepository userRepository;
	final UserService userService;
	final RoleRepository roleRepository;
	final PasswordEncoder encoder;
	final JwtUtils jwtUtils;

	public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.userService = userService;
		this.roleRepository = roleRepository;
		this.encoder = encoder;
		this.jwtUtils = jwtUtils;
	}

	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		var authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt,
				userDetails.getId(),
				userDetails.getUsername(),
				userDetails.getEmail(),
				roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
			return ResponseEntity
					.badRequest()
					.body(MessageResponse.builder().message("Error: Username is already taken!").build());
		}

		if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
			return ResponseEntity
					.badRequest()
					.body(MessageResponse.builder().message("Error: Email is already in use!").build());
		}

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			var userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND_ERROR_MESSAGE));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				if ( ERole.ROLE_ADMIN.name().equals(role)) {
					var adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND_ERROR_MESSAGE));
					roles.add(adminRole);
				} else {
					var userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND_ERROR_MESSAGE));
					roles.add(userRole);
				}
			});
		}

		var user = User.builder()
				.username(signUpRequest.getUsername())
				.email(signUpRequest.getEmail())
				.password(encoder.encode(signUpRequest.getPassword()))
				.roles(roles)
				.build();

		userService.save(user);

		return ResponseEntity.ok(MessageResponse.builder().message("User registered successfully!").build());
	}
}
