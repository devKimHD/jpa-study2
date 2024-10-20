package jpabook.jpashop.domain.repository.order.simplequery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
//화면용 DTO용도
public class OrderSimpleQueryRepository {
    private EntityManager em;
    public List<OrderSimpleQueryDto> findOrderDtos()
    {
        return  em.createQuery(
                "select new jpabook.jpashop.domain.repository.order.simplequery.OrderSimpleQueryDto(" +
                        "o.id, m.name, o.orderDate, o.status, d.address" +
                        ") o from Order o " +
                        " join o.member m " +
                        " join o.delivery d", OrderSimpleQueryDto.class).getResultList();

    }
}
