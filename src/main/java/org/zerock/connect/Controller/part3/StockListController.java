package org.zerock.connect.Controller.part3;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.connect.Service.part3.ReceiveService;
import org.zerock.connect.Service.part3.ReleasesDTO;
import org.zerock.connect.Service.part3.ReleasesService;
import org.zerock.connect.entity.ContractItem;
import org.zerock.connect.entity.Member;
import org.zerock.connect.entity.Receive;
import org.zerock.connect.entity.Releases;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/part3")
public class StockListController {

    @Autowired
    ReleasesService releasesService;

    @Autowired
    ReceiveService receiveService;


    //  재고현황관리
//    전체조회
    @GetMapping("/stockList")
    public String stockList(Model model , @PageableDefault(size = 15, sort = "totalOrderCount", direction = Sort.Direction.ASC) Pageable pageable, HttpSession session) {
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }
        System.out.println("재고현황관리");

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
        List<Receive> receiveList = receiveService.getAllReceive();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), receiveList.size());


        List<Receive> pageContent = receiveList.subList(start, end);
        Page<Receive> receivesLists = new PageImpl<>(pageContent, pageable, receiveList.size());
        model.addAttribute("receivesLists", receivesLists);
        return "/part3/stockList";
    }
    //    날짜별로 조회
    @GetMapping("/stockDateList")
    public String stockDateList(Model model , @RequestParam("start_date")LocalDate start_date, @RequestParam("end_date") LocalDate end_date ,@PageableDefault(size = 1, sort = "totalOrderCount", direction = Sort.Direction.ASC) Pageable pageable,HttpSession session) {
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }
        System.out.println("재고 날짜별 조회");

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
        List<Receive> receiveList = receiveService.searchReceiveDate(start_date,end_date);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), receiveList.size());


        List<Receive> pageContent = receiveList.subList(start, end);
        Page<Receive> receivesLists = new PageImpl<>(pageContent, pageable, receiveList.size());
        model.addAttribute("receivesLists", receivesLists);
        return "/part3/stockList";
    }

//    자재관리 그래프 대분류 , 중분류 , 소분류로 보여주기
    @GetMapping("/stockUnitlistgragh")
    public String stockListgragh(Model model,HttpSession session){
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }

        List<Object[]> graphData = releasesService.groupbyUnitcode();
        model.addAttribute("graphData",graphData);
        System.out.println("그래프 데이터"+graphData);

        return "/part3/StockgraphForm";
    }

    @GetMapping("/stockAssylistgragh")
    public String stockAssyListgragh(Model model,HttpSession session){
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }

        List<Object[]> graphData = releasesService.groupbyAssycode();
        model.addAttribute("graphData",graphData);
        System.out.println("그래프 데이터"+graphData);

        return "/part3/StockgraphForm";
    }

    @GetMapping("/stockPartlistgragh")
    public String stockPartlistgragh(Model model,HttpSession session){
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }

        List<Object[]> graphData = releasesService.groupbyPartcode();
        model.addAttribute("graphData",graphData);
        System.out.println("그래프 데이터"+graphData);

        return "/part3/StockgraphForm";
    }


}
