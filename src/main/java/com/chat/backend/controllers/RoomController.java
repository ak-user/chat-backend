package com.chat.backend.controllers;

import com.chat.backend.models.Room;
import com.chat.backend.models.User;
import com.chat.backend.payload.request.RoomRequest;
import com.chat.backend.service.RoomService;
import com.chat.backend.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/room")
public class RoomController {

	private final RoomService roomService;
	private final UserService userService;

	public RoomController(RoomService roomService, UserService userService) {
		this.roomService = roomService;
		this.userService = userService;
	}

	@PostMapping("/create")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Room save(@RequestBody RoomRequest roomRequest) {

		Set<User> connections = new HashSet<>();

		roomRequest.getConnections().forEach(userId -> connections.add(userService.findById(userId)));

		var room = Room.builder()
				.name(roomRequest.getName())
				.connections(connections)
				.build();

		return roomService.save(room);
	}

	@GetMapping("/all")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public List<Room> getAllRoomsByUserId(@RequestParam Long userId) {
		User user = userService.findById(userId);
		return roomService.findAllRoomsByUser(user);
	}

	@GetMapping
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Room getRoomById(@RequestParam Long id) {
		return roomService.findById(id);
	}

	@DeleteMapping
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteRoomById(@RequestParam Long id) {
		roomService.deleteRoomById(id);
	}
}
