package org.zerock.connect.Service.part2;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.connect.entity.Orders;
import org.zerock.connect.entity.Progress;
import org.zerock.connect.entity.Receive;
import org.zerock.connect.repository.OrdersRepository;
import org.zerock.connect.repository.ProgressRepository;
import org.zerock.connect.repository.ReceiveRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProgressService {
    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    ProgressRepository progressRepository;

    @Autowired
    ReceiveRepository receiveRepository;

    //검수 예정 품목 리스트 아작스
    public List<Orders> progressScheduleAjax(String itemCode, String itemName, LocalDate startDate, LocalDate endDate) {
        return ordersRepository.progressScheduleAjax(itemCode, itemName, startDate, endDate);
    }

    public List<Orders> findProgressScheduleList(String itemCode, String itemName) {
        return ordersRepository.findProgressScheduleList(itemCode, itemName);
    }

    //검수 선택 api
    public Orders progressChoiceAjax(Long orderNum) {
        return ordersRepository.progressChoiceAjax(orderNum);
    }

    // max 서비스 추가
    public Progress getMaxProgress(Long orderNum) {
        return progressRepository.getMaxProgress(orderNum);
    }

    //검수 저장 API
    public Progress saveProgress(Progress progress) {
        return progressRepository.save(progress);
    }

    public  List<Progress> progressListAjax(Long orderNum) {
        return progressRepository.progressListAjax(orderNum);
    }


    public Integer totalAmount(Long orderNum) {
        return progressRepository.totalAmount(orderNum);
    }

    public Receive save(Receive receive) {
        return  receiveRepository.save(receive);
    }

    @Transactional
    public Integer deleteProgressAjax(Long progressNum) {
        return progressRepository.deleteByProgressNum(progressNum);
    }

    //검수 처리 저장
    @Transactional
    public Integer updateProgress(Integer progressAmount, String progressResult, double percent, Long progressNum) {
        return progressRepository.updateProgress(progressAmount, progressResult, (int) percent, progressNum);
    }

    public Orders getOrderCount(Long orderNum) {
        return ordersRepository.progressChoiceAjax(orderNum);
    }

    // 100% 이면 남은 등록 차수를 지우기
    @Transactional
    public Integer deleteProgress(Long orderNum, Long progressNum) {
        return progressRepository.deleteProgress(orderNum, progressNum);
    }
}
