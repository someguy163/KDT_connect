package org.zerock.connect.Controller.part3;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.connect.Service.part3.ReceiveService;
import org.zerock.connect.Service.part3.ReleasesService;
import org.zerock.connect.entity.Member;
import org.zerock.connect.entity.Product;
import org.zerock.connect.entity.Receive;
import org.zerock.connect.entity.Releases;
import org.zerock.connect.repository.ReceiveRepository;
import org.zerock.connect.repository.ReleasesRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/part3")
public class ReleaseController {

    @Autowired
    private ReceiveService receiveService;

    @Autowired
    private ReleasesService releasesService;

    @Autowired
    private ReleasesRepository releasesRepository;

    // 출고 리스트 뷰
    @GetMapping("/releaseList")
    public String getAllReleases(Model model, HttpSession session) {
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }
        System.out.println("출고관리");

        // 입고 완료 품목
        List<Receive> receiveList = receiveService.getAllReceiveSortedByReceiveDate();
        model.addAttribute("receiveList", receiveList);

        return "/part3/releaseList";
    }

    // 출고수량 저장
    @PostMapping("/saveReleases")
    public String saveRelease(@RequestParam("receiveNum") Long receiveNum, Releases releases,
                              @RequestParam("releaseCount") Integer releaseCount,
                              @RequestParam("releaseDate") String releaseDate,
                              Model model , HttpServletResponse response) throws IOException {

        // 입고 조회
        Receive selectReceive = receiveService.findByReceiveNum(receiveNum);


        if(selectReceive.getReceiveCount() < releaseCount){
            System.out.println("출고 가능수량 초과");
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('출고수량이 재고 수량보다 많습니다.'); location.href='/part3/releaseList';</script>");
            writer.flush();
            return null;
        }
        else {
            selectReceive.setReceiveCount(selectReceive.getReceiveCount() - releaseCount);
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('출고 처리가 완료되었습니다.'); location.href='/part3/releaseList';</script>");
            writer.flush();
            releases.setReceive(selectReceive);
            // 출고테이블 인서트
            Releases resultReleases = releasesService.save(releases);
            return null;
        }

        // 출고 저장
    }


    @GetMapping("searchreleaselist")
    public String searchreleaselist(@RequestParam("itemName") String itemName,Model model,HttpSession session) {
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }

        List<Receive> searchreleaselist = receiveService.searchreleaselist(itemName);

        model.addAttribute("receiveList",searchreleaselist);

        return "/part3/releaseList";
    }

}
