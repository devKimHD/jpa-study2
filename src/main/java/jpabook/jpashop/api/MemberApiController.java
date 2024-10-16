package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }
    @GetMapping("/api/v2/members")
    public Result MemberV2()
    {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        // json을 변경가능하게 반환 위해 data로 감싸줌
        return new Result(collect.size(),collect);
    }
    @Data
    @AllArgsConstructor
    static class Result<T>
    {
        private int count;
        private T data;
    }
    @Data
    @AllArgsConstructor
    static class MemberDto
    {
        private String name;

    }
    @PostMapping("/api/v1/members")
    public CteateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CteateMemberResponse(id);
    }
    //api 변화에 대응하기 위한 DTO로 변경
    @PostMapping("/api/v2/members")
    public CteateMemberResponse saveMemberV2(@RequestBody @Valid CtrateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CteateMemberResponse(id);
    }
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request)
    {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }
    @Data
    static class UpdateMemberRequest {
        private String name;
    }
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
    @Data
    static class CtrateMemberRequest {
        //api spec에 맞춰서 validation을 해야함
        //side effect 막기 위한 선택
        @NotEmpty
        private String name;

    }
    @Data
    static class CteateMemberResponse {
        private Long id;

        public CteateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
