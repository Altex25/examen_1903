package com.coworking.memberservice.kafka;

import com.coworking.memberservice.event.MemberStatusEvent;
import com.coworking.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventConsumer {

    private final MemberRepository memberRepository;

    @KafkaListener(topics = "member-status", groupId = "member-service-group")
    public void onMemberStatus(MemberStatusEvent event) {
        log.info("Received member-status event: memberId={}, suspended={}", event.memberId(), event.suspended());
        memberRepository.findById(event.memberId()).ifPresent(member -> {
            member.setSuspended(event.suspended());
            memberRepository.save(member);
        });
    }
}
