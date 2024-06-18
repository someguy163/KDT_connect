package org.zerock.connect.Service.part2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.connect.entity.Orders;
import org.zerock.connect.entity.ProcurementPlan;
import org.zerock.connect.entity.Progress;
import org.zerock.connect.repository.OrdersRepository;
import org.zerock.connect.repository.ProcurementPlanRepository;
import org.zerock.connect.repository.ProgressRepository;
import org.zerock.connect.repository.ReceiveRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class PurchaseOrderService {
    @Autowired
    ProcurementPlanRepository procurementPlanRepository;

    @Autowired
    ProgressRepository progressRepository;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    ReceiveRepository receiveRepository;

    //발주 품목 선택 아작스 구현
    public List<ProcurementPlan> procurementPlanListAjax(String comName, String itemName, LocalDate startDate, LocalDate endDate) {
        return procurementPlanRepository.procurementPlanListAjax(comName, itemName, startDate, endDate);
    }

    //발주 품목 선택 api
    public ProcurementPlan orderChoiceAjax(Long planNum) {
        return procurementPlanRepository.orderChoiceAjax(planNum);
    }

    public List<ProcurementPlan> findPlanList(String comName, String itemName) {
        return procurementPlanRepository.findPlanList(comName, itemName);
    }

    //발주 저장 api
    public Orders saveOrder(Orders orders) {
        return ordersRepository.save(orders);
    }

    //발주 리스트 아작스 구현
    public List<Orders> orderListAjax(String comName, String itemName, LocalDate startDate, LocalDate endDate) {
        return ordersRepository.orderListAjax(comName, itemName, startDate, endDate);
    }

    //발주 리스트 찾기
    public List<Orders> findOrderList(String comName, String itemName) {
        return ordersRepository.findOrderList(comName, itemName);
    }

    //발주 마감 아작스 API
    @Transactional
    public Integer orderDeadlineAjax(Long orderNum) {
        return ordersRepository.orderDeadlineAjax(orderNum);
    }

    //발주서 삭제 AJAX api
    @Transactional
    public Integer deleteOrderAjax(Long orderNum) {
        return ordersRepository.deleteOrderAjax(orderNum);
    }

    //진척 검수 삭제
    @Transactional
    public Integer deletePlan(Long orderNum) {
        return progressRepository.deletePlan(orderNum);
    }

    //입고 예정 삭제
    @Transactional
    public Integer deleteReceive(Long orderNum) {
        return receiveRepository.deleteReceive(orderNum);
    }

}
