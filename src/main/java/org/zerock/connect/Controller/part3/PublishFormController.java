package org.zerock.connect.Controller.part3;


import jakarta.servlet.http.HttpSession;
import lombok.experimental.PackagePrivate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.connect.Service.part3.PublishService;
import org.zerock.connect.Service.part3.ReceiveService;
import org.zerock.connect.entity.Member;
import org.zerock.connect.entity.Publish;
import org.zerock.connect.entity.Receive;
import org.zerock.connect.repository.ReceiveRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/part3")
public class PublishFormController {

    @Autowired
    ReceiveService receiveService;

    @Autowired
    PublishService publishService;

    @Autowired
    ReceiveRepository receiveRepository;

    // 거래명세서발행페이지
    @GetMapping("/publishForm")
    public String publishForm(Model model,HttpSession session) {
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }
        System.out.println("입고완료품목");

        // 입고 완료 품목
        List<Receive> receiveList = receiveService.findReceiveNotInPublishAndReceiveYn();
        model.addAttribute("receiveList", receiveList);

        System.out.println("거래명세서 발행 완료 리스트");

        List<Publish> publishList = publishService.getAllPublish();
        model.addAttribute("publishList", publishList);

        return "/part3/publishForm";
    }

    // 검색
    @GetMapping("/searchReceive")
    public String searchReceive(
            @RequestParam("searchText") String keyword, // 입력한 검색어
            Model model,HttpSession session
    ) {
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }
        List<Receive> searchResult = receiveService.searchReceive(keyword);
        model.addAttribute("receiveList", searchResult);

        List<Publish> publishList = publishService.getAllPublish();
        model.addAttribute("publishList", publishList);

        return "/part3/publishForm";
    }

    // 거래명세서 저장
    @PostMapping("/savePublish")
    public String savePublish(Publish publish , HttpSession session, @RequestParam("receiveNum") Long receiveNum, RedirectAttributes redirectAttributes) {

//        Receive selectReceive = receiveService.findByReceiveNum(receiveNum);

        // 세션에서 현재 로그인된 사용자 정보 가져오기
        Member loginedUser = (Member) session.getAttribute("loginedUser");

        // receiveNum을 사용하여 해당하는 Receive 객체 가져오기
        Receive receive = receiveService.findByReceiveNum(receiveNum);
        if (receive == null) {
            throw new IllegalArgumentException("Invalid receiveNum: " + receiveNum);
        }

        // publish 객체의 receive 필드에 가져온 Receive 객체 설정
        publish.setReceive(receive);
        publish.setInvoiceDate(LocalDate.now());
        publish.setPublisher(loginedUser.getMemberId());

        try {
            // 중복 체크 -이미 존재하는 receiveNum인지 확인
            publishService.save(publish);
            redirectAttributes.addFlashAttribute("successMessage", "거래명세서 발행 완료.");
            System.out.println("저장성공");
        } catch (IllegalArgumentException e) {
            // 중복이 있을 경우 예외를 받아 처리
            redirectAttributes.addFlashAttribute("errorMessage", "이미 발행된 거래명세서입니다.");
            System.out.println("저장실패");
        }

        return "redirect:/part3/publishForm";
    }

    @GetMapping("/invoice")
    public String getInvoiceDetails(@RequestParam("invoiceNumber") Long invoiceNumber, Model model) {
        System.out.println("완료된 거래명세서 모달창");
        Publish invoiceDetails = publishService.getInvoiceDetailsByNumber(invoiceNumber);
        model.addAttribute("invoiceDetails", invoiceDetails);
        return "/part3/invoiceDetailsModal";
    }









}
