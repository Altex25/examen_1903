package com.coworking.reservationservice.event;

public record MemberStatusEvent(Long memberId, boolean suspended) {}
