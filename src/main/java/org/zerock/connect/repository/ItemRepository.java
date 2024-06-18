package org.zerock.connect.repository;

import org.hibernate.annotations.SQLSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.connect.Service.part1.ItemService;
import org.zerock.connect.entity.ContractItem;
import org.zerock.connect.entity.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByProductProductId(String productId);

    Item findByItemIndex(Long itemIndex);



//   계약 품목테이블에서 계약 안된애들만 보여지게
    @Query(value = "SELECT I FROM Item I WHERE I.itemIndex != all (SELECT C.item.itemIndex FROM ContractItem C)")
    List<Item> NocontractItem();

//    List<Item> deleteItemByItemIndex(Item itemIndex);



}
