package org.zerock.connect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.connect.entity.ContractItem;

import java.util.List;

@Repository
public interface ContractItemRepository extends JpaRepository <ContractItem, Long> {


    @Query(value = "SELECT C, P, I " +
            "FROM ContractItem C JOIN Item I " +
            "ON C.item.itemIndex = I.itemIndex " +
            "JOIN  Product P " +
            "on I.product.productId = P.productId ")
    List<ContractItem> ContractItemList();



    @Query(value = "SELECT C, P, I " +
            "FROM ContractItem C JOIN Item I " +
            "ON C.item.itemIndex = I.itemIndex " +
            "JOIN  Product P " +
            "on I.product.productId = P.productId " +
            "WHERE C.conitemNo = :conitemNo")
    List<ContractItem> selectByConitemNo(@Param("conitemNo") Long conitemNo);

    ContractItem findByConitemNo(Long conitemNo);

    @Query(value = "SELECT C, P, I " +
            "FROM ContractItem C JOIN Item I " +
            "ON C.item.itemIndex = I.itemIndex " +
            "JOIN  Product P " +
            "on I.product.productId = P.productId " +
            "where " +
            "C.item.itemName like %:itemName%")
    List<ContractItem> selectByConitemName(@Param("itemName") String itemName);


    @Query(value = "select count(C) from ContractItem C ")
    int countContractItemcount();
}
