package jpabook.jpashop.domain.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

//    @PersistenceContext
//    @Autowired 스프링 부트가 위에 어노테이션 안써도 되게 해줌
    private final EntityManager em;
    

    public void save(Member member){em.persist(member);}
    public Member findOne(Long id)
    {
        return  em.find(Member.class,id);
    }
    public List<Member> findAll()
    {
        return em.createQuery("select m from Member m", Member.class).getResultList();//테이블이 아닌 엔티티 객체로
    }

    public List<Member> findByName(String name)
    {
        return em.createQuery("select m from Member m where m.name= :name",Member.class).setParameter("name",name).getResultList();
    }
}
