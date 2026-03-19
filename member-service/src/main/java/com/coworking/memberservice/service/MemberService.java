package com.coworking.memberservice.service;

import com.coworking.memberservice.entity.Member;
import com.coworking.memberservice.entity.SubscriptionType;
import com.coworking.memberservice.kafka.MemberEventProducer;
import com.coworking.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberEventProducer memberEventProducer;

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found: " + id));
    }

    public Member create(Member member) {
        member.setMaxConcurrentBookings(resolveMaxBookings(member.getSubscriptionType()));
        member.setActiveBookings(0);
        member.setSuspended(false);
        return memberRepository.save(member);
    }

    public Member update(Long id, Member member) {
        Member existing = findById(id);
        existing.setFullName(member.getFullName());
        existing.setEmail(member.getEmail());
        existing.setSubscriptionType(member.getSubscriptionType());
        existing.setMaxConcurrentBookings(resolveMaxBookings(member.getSubscriptionType()));
        return memberRepository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        memberRepository.deleteById(id);
        memberEventProducer.publishMemberDeleted(id);
    }

    public boolean isSuspended(Long id) {
        return findById(id).isSuspended();
    }

    /**
     * Incrémente le compteur de réservations actives.
     * La mise à jour du champ suspended est gérée de façon asynchrone via Kafka
     * par le Reservation Service qui publie un événement member-status.
     */
    public Member incrementActiveBookings(Long id) {
        Member member = findById(id);
        member.setActiveBookings(member.getActiveBookings() + 1);
        return memberRepository.save(member);
    }

    /**
     * Décrémente le compteur de réservations actives.
     * La mise à jour du champ suspended est gérée de façon asynchrone via Kafka
     * par le Reservation Service qui publie un événement member-status.
     */
    public Member decrementActiveBookings(Long id) {
        Member member = findById(id);
        int updated = Math.max(0, member.getActiveBookings() - 1);
        member.setActiveBookings(updated);
        return memberRepository.save(member);
    }

    private int resolveMaxBookings(SubscriptionType type) {
        return switch (type) {
            case BASIC -> 2;
            case PRO -> 5;
            case ENTERPRISE -> 10;
        };
    }
}
