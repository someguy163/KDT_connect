package org.zerock.connect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zerock.connect.entity.Orders;
import org.zerock.connect.entity.Progress;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    //발주 리스트 아작스 구현
    @Query("select o,p,ci,i,c,r from Orders o " +
            "inner join ProcurementPlan p on o.procurementPlan.planNum = p.planNum " +
            "inner join ContractItem ci on p.contractItem.conitemNo = ci.conitemNo " +
            "inner join Item i on ci.item.itemIndex = i.itemIndex " +
            "inner join Company c on ci.company.businessId = c.businessId " +
            "left join Receive r on o.orderNum = r.orders.orderNum " +
            "where o.orderDate between :startDate and :endDate and c.comName like concat('%', :comName, '%') and i.itemName like concat('%',:itemName,'%') " +
            "order by p.orders.receiveDueDate")
    List<Orders> orderListAjax(@Param("comName") String comName, @Param("itemName") String itemName, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    //발주 리스트 전체 조회
    @Query("select o,p,ci,i,c from Orders o " +
            "inner join ProcurementPlan p on o.procurementPlan.planNum = p.planNum " +
            "inner join ContractItem ci on p.contractItem.conitemNo = ci.conitemNo " +
            "inner join Item i on ci.item.itemIndex = i.itemIndex " +
            "inner join Company c on ci.company.businessId = c.businessId " +
            "left join Receive r on o.orderNum = r.orders.orderNum " +
            "where c.comName like concat('%', :comName, '%') and i.itemName like concat('%',:itemName,'%') " +
            "order by p.orders.receiveDueDate")
    List<Orders> findOrderList(@Param("comName") String comName, @Param("itemName") String itemName);

    //발주 마감 아작스 API
    @Modifying
    @Query("update Orders set orderYn = 'Y' where orderNum =:orderNum")
    Integer orderDeadlineAjax(@Param("orderNum") Long orderNum);

    //발주서 삭제 AJAX api
    @Modifying
    @Query("delete from Orders where orderNum =:orderNum")
    Integer deleteOrderAjax(@Param("orderNum") Long orderNum);

    //검수 예정 품목 리스트 아작스
    @Query("select o,p,r,pl,ci,i from Orders o " +
            "left join Progress p on o.orderNum = p.orders.orderNum " +
            "left join Receive r on o.orderNum = r.orders.orderNum " +
            "inner join ProcurementPlan pl on o.procurementPlan.planNum = pl.planNum " +
            "inner join ContractItem ci on pl.contractItem.conitemNo = ci.conitemNo " +
            "inner join Item i on ci.item.itemIndex = i.itemIndex " +
            "where (p IS NULL OR (p.progressCount = (SELECT MAX(p2.progressCount) FROM Progress p2 WHERE p2.orders.orderNum = o.orderNum))) " +
            "and (r.receiveYn is null or r.receiveYn = 'N') and o.orderDate between :startDate and :endDate and i.itemName like concat('%', :itemName, '%') and i.itemCode like concat('%', :itemCode, '%') " +
            "order by o.orderNum ASC")
    List<Orders> progressScheduleAjax(@Param("itemCode") String itemCode, @Param("itemName") String itemName, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("select o,p,r,pl,ci,i from Orders o " +
            "left join Progress p on o.orderNum = p.orders.orderNum " +
            "left join Receive r on o.orderNum = r.orders.orderNum " +
            "inner join ProcurementPlan pl on o.procurementPlan.planNum = pl.planNum " +
            "inner join ContractItem ci on pl.contractItem.conitemNo = ci.conitemNo " +
            "inner join Item i on ci.item.itemIndex = i.itemIndex " +
            "where (p IS NULL OR (p.progressCount = (SELECT MAX(p2.progressCount) FROM Progress p2 WHERE p2.orders.orderNum = o.orderNum))) " +
            "and (r.receiveYn is null or r.receiveYn = 'N') and i.itemName like concat('%', :itemName, '%') and i.itemCode like concat('%', :itemCode, '%')" +
            "order by o.orderNum ASC")
    List<Orders> findProgressScheduleList(@Param("itemCode") String itemCode, @Param("itemName") String itemName);

    //검수 선택 api
    @Query("select o,pl,c,ci,i from Orders o " +
            "inner join ProcurementPlan pl on o.procurementPlan.planNum = pl.planNum " +
            "inner join ContractItem ci on pl.contractItem.conitemNo = ci.conitemNo " +
            "inner join Company c on ci.company.businessId = c.businessId " +
            "inner join Item i on ci.item.itemIndex = i.itemIndex " +
            "where o.orderNum =:orderNum ")
    Orders progressChoiceAjax(@Param("orderNum") Long orderNum);


    @Query(value = "select count(o) from Orders o")
    int findAllorderscount();

    @Query("select o.orderCount from Orders o where o.orderNum =:orderNum")
    Integer getOrderCount(@Param("orderNum") Long orderNum);
}
