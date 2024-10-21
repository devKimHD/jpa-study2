package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.repository.MemberRepository;
import jpabook.jpashop.domain.repository.MemberRepositoryOld;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 기본셋팅을 해놓고 join 같은거만 readonly 없애기 함수 비율 따라
@RequiredArgsConstructor // final 붙은 필드로 생성자 자동생성 lombok
public class MemberService {

//    @Autowired
    // 컴파일 시점 체크 위한 final
    private final MemberRepository memberRepository;

    //최신버전에서는 생성자 하나면 자동으로 인젝션


    //join
    /**
     * 회원가입
     * */
    @Transactional //spring transactional
    public Long join(Member member)
    {
        validateDuplicateMember(member);
        //동시 가입 방지 디비내에 unique 조건 줄것
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member)
    {
        //Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty())
        {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }
    //find list

    public List<Member> findMembers()
    {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId)
    {
        return memberRepository.findById(memberId).get();
    }

    @Transactional
    public void update(Long id, String name)
    {
        Member member = memberRepository.findById(id).get();
        member.setName(name);
    }
}
