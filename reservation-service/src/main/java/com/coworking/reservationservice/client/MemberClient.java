package com.coworking.reservationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "member-service")
public interface MemberClient {

    @GetMapping("/api/members/{id}/suspended")
    boolean isSuspended(@PathVariable("id") Long id);

    @PostMapping("/api/members/{id}/booking/increment")
    void incrementBookings(@PathVariable("id") Long id);

    @PostMapping("/api/members/{id}/booking/decrement")
    void decrementBookings(@PathVariable("id") Long id);
}
