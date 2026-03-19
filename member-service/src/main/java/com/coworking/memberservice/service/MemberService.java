package com.coworking.memberservice.service;

import com.coworking.memberservice.entity.Member;
import com.coworking.memberservice.entity.SubscriptionType;
import com.coworking.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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
        memberRepository.deleteById(id);
    }

    public boolean isSuspended(Long id) {
        return findById(id).isSuspended();
    }

    public Member incrementActiveBookings(Long id) {
        Member member = findById(id);
        member.setActiveBookings(member.getActiveBookings() + 1);
        if (member.getActiveBookings() >= member.getMaxConcurrentBookings()) {
            member.setSuspended(true);
        }
        return memberRepository.save(member);
    }

    public Member decrementActiveBookings(Long id) {
        Member member = findById(id);
        int updated = Math.max(0, member.getActiveBookings() - 1);
        member.setActiveBookings(updated);
        if (updated < member.getMaxConcurrentBookings()) {
            member.setSuspended(false);
        }
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
