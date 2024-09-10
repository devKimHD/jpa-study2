package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.repository.ItemRepository;
import jpabook.jpashop.domain.repository.MemberRepository;
import jpabook.jpashop.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    //order
    /**
     * 주문
     * */
    @Transactional
    public Long order(Long memberId, Long itemId, int count)
    {
        //entity
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);
        //address
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //orderItem
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        //order
        Order order = Order.createOrder(member, delivery, orderItem);
//        Order order = Order.createOrder(member, delivery, orderItem, orderItem1, orderItem2);

        //order save
        orderRepository.save(order);
        return order.getId();
    }
    //cancel
    @Transactional
    public void cancelOrder(Long orderId)
    {
        //엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 캔슬
        order.cancel();
    }
    //search
//    public List<Order> findOrders(OrderSearch orderSearch)
//    {
//        return orderRepository.findAll(orderSearch);
//    }
}
