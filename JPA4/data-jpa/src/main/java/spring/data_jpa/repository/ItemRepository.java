package spring.data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.data_jpa.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
