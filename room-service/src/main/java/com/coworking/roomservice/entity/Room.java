package com.coworking.roomservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String city;
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    private BigDecimal hourlyRate;
    private boolean available;
}
