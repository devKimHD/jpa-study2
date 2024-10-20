package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.repository.OrderRepository;
import jpabook.jpashop.domain.repository.OrderSearch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController
{
    private final OrderRepository orderRepository;
    @GetMapping("/api/v1/orders")
    private List<Order> ordersV1()
    {
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : all)
        {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o->o.getItem().getName());
         }
        return all;
    }

    @GetMapping("/api/v2/orders")
    private List<OrderDto> ordersV2()
    {
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        List<OrderDto> orderDtos = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return orderDtos;
    }
    @GetMapping("/api/v3/orders")
    private List<OrderDto> ordersV3()
    {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> orderDtos = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return orderDtos;
    }
    @Getter
    static class OrderDto
    {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order)
        {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            // api spec에서 엔티티는 내부에 있는 엔티티도 미노출 할것
            orderItems = order.getOrderItems().stream().map(orderItem -> new OrderItemDto(orderItem)).collect(Collectors.toList());
        }
    }
    @Getter

    static class OrderItemDto
    {
        private String itemName; // 상품 명
        private int orderPrice; // 주문 가격
        private int count; // 주문 수량
        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
