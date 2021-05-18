package com.chat.backend.service;

import com.chat.backend.controllers.NotFoundException;
import com.chat.backend.models.Message;
import com.chat.backend.models.Room;
import com.chat.backend.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message save(Message message) {
         return messageRepository.save(message);
    }

    public List<Message> findAllMessagesByRoom(Room room) {
        Optional<List<Message>> optionalMessages = messageRepository.findAllByRoom(room);

        if (optionalMessages.isPresent()) {
            return optionalMessages.get();
        } else {
            throw new NotFoundException("Messages by this Room ID are not found");
        }
    }

    public void deleteMessageById(Long id) {
        messageRepository.deleteById(id);
    }

}
