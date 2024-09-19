package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void save(Item item)
    {
        itemRepository.save(item);
    }
    public List<Item> findItems()
    {
        return itemRepository.findAll();
    }
    public Item findOne(Long itemId)
    {
        return itemRepository.findOne(itemId);
    }
    //이방식 변경감지
    @Transactional
    public Item updateItem(Long itemId,  String name, int price, int stockQuantity)
    {
        //jpa가 관리하는 엔티티 우선 생성 후 기존 변경된 파라미터 셋팅
        Item findItem = itemRepository.findOne(itemId);
//        findItem.change(name,price,stockQuantity);
//        이런식으로 유지보수 할때 알아볼수 있게 함수화 시키는게 좋은 소스
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
        //...업데이트할 필드 채우기
        //호출 불필요
        //itemRepository.save(findItem);
        return findItem;
    }
}
