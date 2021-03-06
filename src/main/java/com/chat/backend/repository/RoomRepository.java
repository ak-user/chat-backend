package com.chat.backend.repository;

import com.chat.backend.models.Room;
import com.chat.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<List<Room>> findAllByConnections(User user);

}
