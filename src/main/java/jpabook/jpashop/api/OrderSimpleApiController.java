package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.repository.OrderRepository;
import jpabook.jpashop.domain.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * XToOne(ManyToOne, OneToOne) 관계 최적화
 * Order
 * Order -> Member
 * Order -> Delivery
 * */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1()
    {
        //양방향이라 무한루프 발생
        //@JsonIgnore 추가
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
        }
        return all;
    }
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOderDto> oderV2()
    {
        //오더2개
        //n+1 -> 1+N(주인이 될1을 포함한) n(연관된) -> 회원, 배송
        //EAGER로 바꿔도 쿼리만 이상해짐
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        List<SimpleOderDto> result = orders.stream()
                .map(o -> new SimpleOderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @Data
    static class SimpleOderDto
    {
        public SimpleOderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();//LAZY 초기화

        }

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

    }
}
