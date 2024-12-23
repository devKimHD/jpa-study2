package jpabook.jpashop;

import jakarta.transaction.Transactional;
import jpabook.jpashop.domain.repository.MemberRepositoryOld;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MemberRepositoryTest {
    @Autowired
    MemberRepositoryOld memberRepository;
    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception
    {
//        //given
//        Member member = new Member();
//        member.setUsername("memberA");
//        //when
//        Long saveId = memberRepository.save(member);
//        Member findMember = memberRepository.find(saveId);
//        //then
//        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
//        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
//
//        Assertions.assertThat(findMember).isEqualTo(member);
//        System.out.println("MemberRepositoryTest.findMember ="+findMember);
//        System.out.println("MemberRepositoryTest.member ="+member);

    }

}