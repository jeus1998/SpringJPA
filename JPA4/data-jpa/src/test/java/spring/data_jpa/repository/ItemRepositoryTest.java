package spring.data_jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.data_jpa.entity.Item;
@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Test
    public void persistTest(){
        Item item = new Item();
        item.setId(1L);
        itemRepository.save(item);
    }
}