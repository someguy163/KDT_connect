package org.zerock.connect.Service.part1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.connect.entity.*;
import org.zerock.connect.repository.*;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    PartRepository partRepository;

    @Autowired
    AssyRepository assyRepository;

    @Autowired
    ItemRepository itemRepository;

//    @Autowired
//    ContractItem contractItem;

    public Product findByProductId(String productId) {
        return productRepository.findByProductId(productId);
    }


    //제품선택화면 AJAX + 검색
    //제품 등록 리스트 아작스 + 검색
    public List<Product> productListAjax(String searchText){
        return productRepository.findByProductIdContainingOrderByProductId(searchText);
    }


    public List<Unit> findUnitList(){
        return unitRepository.findAll();
    }
    public List<Part> findPartList(){
        return partRepository.findAll();
    }
    public List<Assy> findAssyList(){
        return assyRepository.findAll();
    }

    public List<Item> findItemList(){
        return itemRepository.findAll();
    }

    public Item saveItem(Item item){
        return itemRepository.save(item);
    }

    // 품목 리스트 아작스
    public List<Item> itemListAjax(String productId) {
        return itemRepository.findByProductProductId(productId);
    }


    public List<Item> NocontractItem() {
        return itemRepository.NocontractItem();
    }

    public Item findByItemIndex(Long itemIndex){
        return itemRepository.findByItemIndex(itemIndex);
    }


//    public List<Item> deleteItemByItemIndex(Item itemIndex){
//        return itemRepository.deleteItemByItemIndex(itemIndex);
//    }

}
