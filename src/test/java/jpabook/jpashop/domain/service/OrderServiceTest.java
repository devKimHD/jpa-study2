package jpabook.jpashop.domain.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.repository.OrderRepository;
import jpabook.jpashop.exception.NotEnoughStockException;
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
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Test
    public void OrderAdd() throws Exception
    {
        //given
        Member member = createMember();
        Book book = createBook("서울 jpa", 10000, 10);
        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("상품 주문시 OREDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품수가 정확해야 한다",1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격*수량",10000*orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량 만큼 재고 감소",8, book.getStockQuantity());
    }


    @Test
    public void orderCancel() throws Exception
    {
        //given
        Member member = createMember();
        Book item = createBook("시골 jpa", 10000, 10);
        int orderCount =2 ;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        //when
        orderService.cancelOrder(orderId);
        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("취소 상태 CANCEL",OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("재고 수량 10개",10, item.getStockQuantity());

    }
    @Test(expected = NotEnoughStockException.class)
    public void orderStockOver() throws Exception
    {
        //given
        Member member = createMember();
        Book book = createBook("서울 jpa", 10000, 10);
        int orderCount = 11;
        //when
        orderService.order(member.getId(), book.getId(), orderCount);
        //then
        fail("재고 주문 수량 부족 예외");
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member= new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","경기","123-123"));
        em.persist(member);
        return member;
    }
}