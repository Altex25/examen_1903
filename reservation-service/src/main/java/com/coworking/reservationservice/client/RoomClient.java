package com.coworking.reservationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "room-service")
public interface RoomClient {

    @GetMapping("/api/rooms/{id}/available")
    boolean isAvailable(@PathVariable("id") Long id);

    @PatchMapping("/api/rooms/{id}/availability")
    void updateAvailability(@PathVariable("id") Long id, @RequestParam("available") boolean available);
}
