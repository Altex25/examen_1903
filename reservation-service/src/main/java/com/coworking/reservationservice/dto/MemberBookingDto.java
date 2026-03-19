package com.coworking.reservationservice.dto;

import lombok.Data;

@Data
public class MemberBookingDto {
    private Long id;
    private Integer activeBookings;
    private Integer maxConcurrentBookings;
    private boolean suspended;
}
