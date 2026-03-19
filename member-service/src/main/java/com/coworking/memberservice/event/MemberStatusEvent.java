package com.coworking.memberservice.event;

public record MemberStatusEvent(Long memberId, boolean suspended) {}
