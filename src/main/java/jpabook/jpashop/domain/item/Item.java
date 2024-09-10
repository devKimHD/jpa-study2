package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="dtype")
public class Item {

    @Id
    @GeneratedValue
    @Column(name="item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직을 여기에 넣는다? 객체 지향적 설계==
    /**
    *재고 증가
     * setter 남발하지 말고 이렇게 쓸것 
    */
    public void addStock(int stockQuantity)
    {
        this.stockQuantity += stockQuantity;
    }
    /**
     *재고 down
     */
    public void removeStock(int stockQuantity)
    {
        int restStock=this.stockQuantity - stockQuantity;
        if (restStock <0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;

    }
}
