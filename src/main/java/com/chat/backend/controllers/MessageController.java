package com.chat.backend.controllers;

import com.chat.backend.models.Message;
import com.chat.backend.payload.request.MessageRequest;
import com.chat.backend.service.MessageService;
import com.chat.backend.service.RoomService;
import com.chat.backend.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/message")
public class MessageController {

	private final MessageService messageService;
	private final UserService userService;
	private final RoomService roomService;

	public MessageController(MessageService messageService, UserService userService, RoomService roomService) {
		this.messageService = messageService;
		this.userService = userService;
		this.roomService = roomService;
	}

	@PostMapping("/create")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Message save(@RequestBody MessageRequest messageRequest) {

		var user = userService.findById(messageRequest.getUserId());
		var room = roomService.findById(messageRequest.getRoomId());

		var message = Message.builder()
				.user(user)
				.room(room)
				.text(messageRequest.getText())
				.build();

		return messageService.save(message);
	}

	@GetMapping("/all")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public List<Message> getAllMessagesFromRoom(@RequestParam Long roomId) {
		var room = roomService.findById(roomId);
		return messageService.findAllMessagesByRoom(room);
	}

	@DeleteMapping
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteMessageById(@RequestParam Long id) {
		messageService.deleteMessageById(id);
	}
}
