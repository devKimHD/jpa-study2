package jpabook.jpashop.domain.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.repository.MemberRepositoryOld;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired
    MemberRepositoryOld memberRepository;
    @Autowired
    EntityManager em;
    @Test
//    @Rollback(value = false)
    public void joinTest() throws Exception
    {
        //given
        Member member = new Member();
        member.setName("kim");
        //when
        Long saveId = memberService.join(member);
        //then
        em.flush(); // 롤백이면 flush 안되어 인서트문 안보이나 이렇게 할시 flush 강제 쿼리는 날리나 rollback
        assertEquals(member, memberRepository.findOne(saveId));
    }
    @Test(expected = IllegalStateException.class)
    public void joinDupTest() throws Exception
    {
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2); // Exception!

//        //then
        fail("예외가 발생한다.");
    }
}