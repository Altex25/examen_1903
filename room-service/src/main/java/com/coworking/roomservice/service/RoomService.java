package com.coworking.roomservice.service;

import com.coworking.roomservice.entity.Room;
import com.coworking.roomservice.kafka.RoomEventProducer;
import com.coworking.roomservice.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomEventProducer roomEventProducer;

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found: " + id));
    }

    public Room create(Room room) {
        return roomRepository.save(room);
    }

    public Room update(Long id, Room room) {
        Room existing = findById(id);
        existing.setName(room.getName());
        existing.setCity(room.getCity());
        existing.setCapacity(room.getCapacity());
        existing.setType(room.getType());
        existing.setHourlyRate(room.getHourlyRate());
        existing.setAvailable(room.isAvailable());
        return roomRepository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        roomRepository.deleteById(id);
        roomEventProducer.publishRoomDeleted(id);
    }

    public boolean isAvailable(Long id) {
        return findById(id).isAvailable();
    }

    public Room updateAvailability(Long id, boolean available) {
        Room room = findById(id);
        room.setAvailable(available);
        return roomRepository.save(room);
    }
}
