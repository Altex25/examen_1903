package com.coworking.memberservice.controller;

import com.coworking.memberservice.entity.Member;
import com.coworking.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public List<Member> findAll() {
        return memberService.findAll();
    }

    @GetMapping("/{id}")
    public Member findById(@PathVariable Long id) {
        return memberService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Member create(@RequestBody Member member) {
        return memberService.create(member);
    }

    @PutMapping("/{id}")
    public Member update(@PathVariable Long id, @RequestBody Member member) {
        return memberService.update(id, member);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        memberService.delete(id);
    }

    @GetMapping("/{id}/suspended")
    public boolean isSuspended(@PathVariable Long id) {
        return memberService.isSuspended(id);
    }

    @PostMapping("/{id}/booking/increment")
    public Member incrementBookings(@PathVariable Long id) {
        return memberService.incrementActiveBookings(id);
    }

    @PostMapping("/{id}/booking/decrement")
    public Member decrementBookings(@PathVariable Long id) {
        return memberService.decrementActiveBookings(id);
    }
}
