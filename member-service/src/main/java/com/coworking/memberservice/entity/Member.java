package com.coworking.memberservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;

    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;

    private boolean suspended;
    private Integer maxConcurrentBookings;
    private Integer activeBookings = 0;
}
