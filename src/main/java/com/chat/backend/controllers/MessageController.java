package com.chat.backend.controllers;

import com.chat.backend.models.Message;
import com.chat.backend.payload.request.MessageRequest;
import com.chat.backend.service.MessageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/message")
public class MessageController {

	private final MessageService messageService;

	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@PostMapping("/create")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Message save(@RequestBody MessageRequest messageRequest) {

		var message = Message.builder()
				.userId(messageRequest.getUserId())
				.roomId(messageRequest.getRoomId())
				.text(messageRequest.getText())
				.build();

		return messageService.save(message);
	}

	@GetMapping("/all")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public List<Message> getAllMessagesFromRoom(@RequestParam Long roomId) {
		return messageService.findAllMessagesByRoomId(roomId);
	}

	@DeleteMapping
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteMessageById(@RequestParam Long id) {
		messageService.deleteMessageById(id);
	}
}
