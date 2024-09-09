package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;
    @Embedded
    private Address address;

//    @Enumerated(EnumType.ORDINAL) // 이러면 중간에 이넘타입 추가시 망함
    @Enumerated(EnumType.STRING) // 이러면 중간에 이넘타입 추가시 망함
    private DeliveryStatus status;
}
