package org.zerock.connect.Service.part3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.connect.entity.Publish;
import org.zerock.connect.entity.Receive;
import org.zerock.connect.repository.PublishRepository;
import org.zerock.connect.repository.ReceiveRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublishService {

    @Autowired
    PublishRepository publishRepository;

    @Autowired
    ReceiveService receiveService;

    public Publish save(Publish publish) {
        // 이미 존재하는 receiveNum인지 확인
        if (existsByReceiveNum(publish.getReceive().getReceiveNum())) {
            throw new IllegalArgumentException("ReceiveNum already exists: " + publish.getReceive().getReceiveNum());
        }

        // 중복이 없다면 저장
        return publishRepository.save(publish);
    }

    public boolean existsByReceiveNum(Long receiveNum) {
        // 이미 존재하는 receiveNum인지 확인하는 메서드
        return publishRepository.existsByReceive_ReceiveNum(receiveNum);
    }

    public List<Publish> getAllPublish() {
        return publishRepository.findAll();
    }

    public Publish getInvoiceDetailsByNumber(Long invoiceNumber) {
        return publishRepository.findByInvoiceNumber(invoiceNumber);
    }






}
