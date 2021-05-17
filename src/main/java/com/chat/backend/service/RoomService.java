package com.chat.backend.service;

import com.chat.backend.controllers.NotFoundException;
import com.chat.backend.models.Room;
import com.chat.backend.models.User;
import com.chat.backend.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room save(Room room) {
         return roomRepository.save(room);
    }

    public List<Room> findAllRoomsByUser(User user) {

        Optional<List<Room>> optionalRooms = roomRepository.findAllByConnections(user);

        if (optionalRooms.isPresent()) {
            return optionalRooms.get();
        } else {
            throw new NotFoundException("Room by this ID is not found");
        }
    }

    public Room findById(Long id) {
        Optional<Room> roomOptional = roomRepository.findById(id);

        if (roomOptional.isPresent()) {
            return roomOptional.get();
        } else {
            throw new NotFoundException("Rooms by this User ID is not found");
        }
    }

    public void deleteRoomById(Long id) {
        roomRepository.deleteById(id);
    }

}
