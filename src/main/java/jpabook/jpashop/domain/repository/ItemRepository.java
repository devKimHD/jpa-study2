package jpabook.jpashop.domain.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository
{
    private final EntityManager em;

    public void save(Item item)
    {
        if (item.getId() == null)
        {
            //id is null add
            em.persist(item);
        }else
        {
            //id is not null edit
            //머지 사용시 모든필드가 셋팅한 값으로 대체, 대체될 당시 null이면 null로 업데이트해서 신중하게 사용 필요
            em.merge(item);
        }
    }
    public Item findOne(Long id)
    {
        return em.find(Item.class,id);
    }
    public List<Item> findAll()
    {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
