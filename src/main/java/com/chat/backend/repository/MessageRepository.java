package com.chat.backend.repository;

import com.chat.backend.models.Message;
import com.chat.backend.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<List<Message>> findAllByRoom(Room room);

}
