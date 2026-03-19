package com.coworking.roomservice.controller;

import com.coworking.roomservice.entity.Room;
import com.coworking.roomservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public List<Room> findAll() {
        return roomService.findAll();
    }

    @GetMapping("/{id}")
    public Room findById(@PathVariable Long id) {
        return roomService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Room create(@RequestBody Room room) {
        return roomService.create(room);
    }

    @PutMapping("/{id}")
    public Room update(@PathVariable Long id, @RequestBody Room room) {
        return roomService.update(id, room);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        roomService.delete(id);
    }

    @GetMapping("/{id}/available")
    public boolean isAvailable(@PathVariable Long id) {
        return roomService.isAvailable(id);
    }

    @PatchMapping("/{id}/availability")
    public Room updateAvailability(@PathVariable Long id, @RequestParam boolean available) {
        return roomService.updateAvailability(id, available);
    }
}
