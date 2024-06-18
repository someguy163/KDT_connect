package org.zerock.connect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zerock.connect.entity.ProcurementPlan;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProcurementPlanRepository extends JpaRepository<ProcurementPlan, Long> {

    //발주 품목 선택 아작스 구현
    @Query("select p,i,ci,c,o from ProcurementPlan p " +
            "inner join ContractItem ci on p.contractItem.conitemNo = ci.conitemNo " +
            "inner join Item i on ci.item.itemIndex = i.itemIndex " +
            "inner join Company c on ci.company.businessId = c.businessId " +
            "left join Orders o on p.planNum = o.procurementPlan.planNum " +
            "where o.orderNum is null and p.planDate between :startDate and :endDate and c.comName like concat('%',:comName,'%') and i.itemName like concat('%',:itemName ,'%') order by p.planDate asc")
    List<ProcurementPlan> procurementPlanListAjax(@Param("comName") String comName, @Param("itemName") String itemName, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    //발주 품목 선택 api
    @Query("Select p,i,ci,c from ProcurementPlan p " +
            "inner join ContractItem ci on p.contractItem.conitemNo = ci.conitemNo " +
            "inner join Item i on ci.item.itemIndex = i.itemIndex " +
            "inner join Company c on ci.company.businessId = c.businessId " +
            "where p.planNum =:planNum ")
    ProcurementPlan orderChoiceAjax(@Param("planNum") Long planNum);

    @Query("Select p,i,ci,c,o from ProcurementPlan p " +
            "inner join ContractItem ci on p.contractItem.conitemNo = ci.conitemNo " +
            "inner join Item i on ci.item.itemIndex = i.itemIndex " +
            "inner join Company c on ci.company.businessId = c.businessId "+
            "left join Orders o on p.planNum = o.procurementPlan.planNum " +
            "where o.orderNum is null and c.comName like concat('%',:comName,'%') and i.itemName like concat('%',:itemName ,'%') order by p.planDate asc")
    List<ProcurementPlan> findPlanList(@Param("comName") String comName, @Param("itemName") String itemName);


    @Query(value = "select count(P) from ProcurementPlan P")
    int findAllprcurementPlancount();

}
