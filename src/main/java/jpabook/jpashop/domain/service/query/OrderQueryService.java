package jpabook.jpashop.domain.service.query;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.repository.OrderRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
//API용도에 맞는 사용
public class OrderQueryService {
    private final OrderRepository orderRepository;
    public List<OrderDto> ordersV3()
    {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> orderDtos = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return orderDtos;
    }

}
