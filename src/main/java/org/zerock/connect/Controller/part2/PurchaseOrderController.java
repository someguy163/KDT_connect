package org.zerock.connect.Controller.part2;

import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.connect.Controller.part1.CompanyController;
import org.zerock.connect.Service.part2.PurchaseOrderService;
import org.zerock.connect.entity.Company;
import org.zerock.connect.entity.Member;
import org.zerock.connect.entity.Orders;
import org.zerock.connect.entity.ProcurementPlan;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/part2")
@Slf4j //로그찍기
public class PurchaseOrderController {

    Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    PurchaseOrderService purchaseOrderService;

    //빈곽 폼 조회
    @GetMapping("/purchaseOrderForm")
    public String purchaseOrderForm(HttpSession session) {
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }
        return "/part2/purchaseOrderForm";
    }

    //조달계획리스트 아작스 구현
    @GetMapping("/procurementPlanListAjax")
    public String procurementPlanListAjax(@RequestParam(value = "searchText", required = false) String searchText,
                                          @RequestParam(value = "searchType", required = false) String searchType,
                                          @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                          @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                          Model model,
                                          @PageableDefault(size = 5, sort = "comName", direction = Sort.Direction.ASC) Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        List<ProcurementPlan> procurementPlanList = new ArrayList<>();

        //동적 쿼리 만들기 위해 서치타입이 comName이면 comName에다가 넘어온 searchText 값을 넣어서 만들어주기
        String comName = "";
        String itemName = "";
        if (searchType.equals("comName")) {
            comName = searchText;
        }
        if (searchType.equals("itemName")) {
            itemName = searchText;
        }

        if (!Objects.isNull(startDate) && !Objects.isNull(endDate)) {
            procurementPlanList = purchaseOrderService.procurementPlanListAjax(comName, itemName, startDate, endDate); // 날짜가 빈값 아닐때 이쪽
        } else {
            procurementPlanList = purchaseOrderService.findPlanList(comName, itemName); //날짜가 비었을때 전체 검색 유도
        }

        int start = (int) pageable.getOffset();//페이지러블 객체에서 알아서 나오는거 >> 사이즈 5으로 설정 싯 페이지를 1로 넘기면 1페이지에 1~10나옴(size가 10이니까) 2면(11~20)
        int end = Math.min((start + pageable.getPageSize()), procurementPlanList.size()); // 5을 계산한 구문

        logger.info("companyList : {}", procurementPlanList);

        List<ProcurementPlan> pageContent = procurementPlanList.subList(start, end); // 데이터가 30개 쌓여있으면  1~10, 11~20, 21~30 이렇게 짤라라
        Page<ProcurementPlan> procurementPlan = new PageImpl<>(pageContent, pageable, procurementPlanList.size()); //현재페이지의 보여줄 리스트, 페이지러블 객체, 전체 리스트 개수(예를 들면 글 30개)
        model.addAttribute("procurementPlanList", procurementPlan);//리스트 객체를 페이징 처리 후  보냄

        return "/part2/procurementPlanListAjax";
    }

    //발주 품목 선택 아작스 api
    @GetMapping("/orderChoiceAjax")
    public String orderChoiceAjax(@RequestParam("planNum") Long planNum, Model model) {
        System.out.println("planNum = " + planNum);
        ProcurementPlan procurementPlan = purchaseOrderService.orderChoiceAjax(planNum);
        model.addAttribute("procurementPlan", procurementPlan);
        return "/part2/orderChoiceAjax";
    }

    //발주 저장 api
    @PostMapping("/saveOrder")
    public String saveOrder(@ModelAttribute Orders orders, @ModelAttribute ProcurementPlan procurementPlan, Model model, HttpServletResponse response) throws IOException {
        orders.setOrderYn("N"); // 발주 마감 여부 N으로 저장
        orders.setProcurementPlan(procurementPlan); // 조인 된 컬럼인 planNum을 orders에 담았다 그래야 save로 orders를 한 번에 객체로 보내서 저장 가능
        Orders result = purchaseOrderService.saveOrder(orders);

        if (result != null) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('발주 등록이 완료되었습니다.');</script>");
            writer.flush();
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('발주 등록에 실패 하였습니다.');</script>");
            writer.flush();
        }
        ProcurementPlan plan = purchaseOrderService.orderChoiceAjax(0L);
        model.addAttribute("procurementPlan", plan);
        return "/part2/orderChoiceAjax";
    }

    //발주 리스트 아작스 api
    @GetMapping("/orderListAjax")
    public String orderListAjax(@RequestParam(value = "searchText", required = false) String searchText,
                                @RequestParam(value = "searchType", required = false) String searchType,
                                @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                Model model,
                                @PageableDefault(size = 5, sort = "comName", direction = Sort.Direction.ASC) Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());


        List<Orders> orders = new ArrayList<>();
        //동적 쿼리 만들기 위해 서치타입이 comName이면 comName에다가 넘어온 searchText 값을 넣어서 만들어주기
        String comName = "";
        String itemName = "";
        if (searchType.equals("comName")) {
            comName = searchText;
        }
        if (searchType.equals("itemName")) {
            itemName = searchText;
        }

        if (!Objects.isNull(startDate) && !Objects.isNull(endDate)) {
            orders = purchaseOrderService.orderListAjax(comName, itemName, startDate, endDate); // 날짜가 빈값 아닐때 이쪽
        } else {
            orders = purchaseOrderService.findOrderList(comName, itemName); //날짜가 비었을때 전체 검색 유도
        }

        int start = (int) pageable.getOffset();//페이지러블 객체에서 알아서 나오는거 >> 사이즈 5으로 설정 싯 페이지를 1로 넘기면 1페이지에 1~10나옴(size가 10이니까) 2면(11~20)
        int end = Math.min((start + pageable.getPageSize()), orders.size()); // 5을 계산한 구문

        List<Orders> pageContent = orders.subList(start, end); // 데이터가 30개 쌓여있으면  1~10, 11~20, 21~30 이렇게 짤라라
        Page<Orders> purchaseOrderList = new PageImpl<>(pageContent, pageable, orders.size()); //현재페이지의 보여줄 리스트, 페이지러블 객체, 전체 리스트 개수(예를 들면 글 30개)
        model.addAttribute("purchaseOrderList", purchaseOrderList);//리스트 객체를 페이징 처리 후  보냄

        return "/part2/orderListAjax";
    }

    //발주 마감 AJAX API
    @PostMapping("/orderDeadlineAjax")
    public String orderDeadlineAjax(@RequestParam(value = "orderNum") Long orderNum,
                                    Model model,
                                    @PageableDefault(size = 5, sort = "comName", direction = Sort.Direction.ASC) Pageable pageable, HttpServletResponse response) throws IOException {

        Integer result = purchaseOrderService.orderDeadlineAjax(orderNum);

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        List<Orders> orders = purchaseOrderService.findOrderList("", "");

        int start = (int) pageable.getOffset();//페이지러블 객체에서 알아서 나오는거 >> 사이즈 5으로 설정 싯 페이지를 1로 넘기면 1페이지에 1~10나옴(size가 10이니까) 2면(11~20)
        int end = Math.min((start + pageable.getPageSize()), orders.size()); // 5을 계산한 구문

        List<Orders> pageContent = orders.subList(start, end); // 데이터가 30개 쌓여있으면  1~10, 11~20, 21~30 이렇게 짤라라
        Page<Orders> purchaseOrderList = new PageImpl<>(pageContent, pageable, orders.size()); //현재페이지의 보여줄 리스트, 페이지러블 객체, 전체 리스트 개수(예를 들면 글 30개)
        model.addAttribute("purchaseOrderList", purchaseOrderList);//리스트 객체를 페이징 처리 후  보냄

        if (result > 0) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('발주 마감이 완료되었습니다.');</script>");
            writer.flush();
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('발주 마감에 실패 하였습니다.');</script>");
            writer.flush();
        }

        return "/part2/orderListAjax";
    }

    //발주서 삭제 AJAX api
    @PostMapping("/deleteOrderAjax")
    public String deleteOrderAjax(@RequestParam(value = "orderNum") Long orderNum,
                                  Model model,
                                  @PageableDefault(size = 5, sort = "comName", direction = Sort.Direction.ASC) Pageable pageable, HttpServletResponse response) throws IOException {

        Integer planResult = purchaseOrderService.deletePlan(orderNum); //진척 검수 계획 먼저 삭제
        Integer receiveResult = purchaseOrderService.deleteReceive(orderNum); //입고 예정 삭제
        Integer result = purchaseOrderService.deleteOrderAjax(orderNum); // 발주서 삭제

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        List<Orders> orders = purchaseOrderService.findOrderList("", "");

        int start = (int) pageable.getOffset();//페이지러블 객체에서 알아서 나오는거 >> 사이즈 5으로 설정 싯 페이지를 1로 넘기면 1페이지에 1~10나옴(size가 10이니까) 2면(11~20)
        int end = Math.min((start + pageable.getPageSize()), orders.size()); // 5을 계산한 구문

        List<Orders> pageContent = orders.subList(start, end); // 데이터가 30개 쌓여있으면  1~10, 11~20, 21~30 이렇게 짤라라
        Page<Orders> purchaseOrderList = new PageImpl<>(pageContent, pageable, orders.size()); //현재페이지의 보여줄 리스트, 페이지러블 객체, 전체 리스트 개수(예를 들면 글 30개)
        model.addAttribute("purchaseOrderList", purchaseOrderList);//리스트 객체를 페이징 처리 후  보냄

        if (result > 0) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('발주서 삭제가 완료되었습니다.');</script>");
            writer.flush();
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('발주서 삭제에 실패 하였습니다.');</script>");
            writer.flush();
        }

        return "/part2/orderListAjax";
    }

}
