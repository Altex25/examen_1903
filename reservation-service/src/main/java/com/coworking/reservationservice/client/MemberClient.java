package com.coworking.reservationservice.client;

import com.coworking.reservationservice.dto.MemberBookingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "member-service")
public interface MemberClient {

    @GetMapping("/api/members/{id}/suspended")
    boolean isSuspended(@PathVariable("id") Long id);

    @PostMapping("/api/members/{id}/booking/increment")
    MemberBookingDto incrementBookings(@PathVariable("id") Long id);

    @PostMapping("/api/members/{id}/booking/decrement")
    MemberBookingDto decrementBookings(@PathVariable("id") Long id);
}
