package jpabook.jpashop.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.repository.order.simplequery.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order)
    {
        em.persist(order);
    }
    public Order findOne(Long id)
    {
        return em.find(Order.class,id);
    }
    // 검색 기능이 포함된 리스트
//    public List<Order> findAll(OrderSearch orderSearch)
//    {
//        String jpql = "select o from Order o join o.member m";
//
//        return em.createQuery("select o from Order o join o.member m"
//                        +" where o.status = :status "
//                        +" and m.name like :name", Order.class)
//                .setParameter("status",orderSearch.getOrderStatus())
//                .setParameter("name",orderSearch.getMemberName())
//                .setMaxResults(1000)
//                .getResultList();
//    }
    /**
     * 유지 보수에 난관 이라 미사용
     * */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName()
                            + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery()
    {
        return  em.createQuery(
                "select o from Order o " +
                        " join fetch o.member m " +
                        " join fetch o.delivery d", Order.class).getResultList();

    }

    public List<OrderSimpleQueryDto> findOrderDtos()
    {
        return  em.createQuery(
                "select new jpabook.jpashop.domain.repository.order.simplequery.OrderSimpleQueryDto(" +
                        "o.id, m.name, o.orderDate, o.status, d.address" +
                        ") o from Order o " +
                        " join o.member m " +
                        " join o.delivery d", OrderSimpleQueryDto.class).getResultList();

    }

    public List<Order> findAllWithItem() {
        return em.createQuery("select distinct o from Order o " +
                " join fetch o.member " +
                " join fetch o.delivery " +
                " join fetch o.orderItems oi " +
                " join fetch oi.item i",Order.class).getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery("select distinct o from Order o " +
                " join fetch o.member " +
                " join fetch o.delivery "
               ,Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit).
                getResultList();
    }
    //querydsl
    public List<Order> findAll(OrderSearch orderSearch)
    {
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();

    }

    private static BooleanExpression nameLike(String memberName) {
        if (!StringUtils.hasText(memberName))
        {
            return null;
        }
        return QMember.member.name.like(memberName);
    }

    private BooleanExpression statusEq(OrderStatus statusCondition)
    {
        if (statusCondition ==null)
        {
            return null;
        }
        return QOrder.order.status.eq(statusCondition);
    }
}
