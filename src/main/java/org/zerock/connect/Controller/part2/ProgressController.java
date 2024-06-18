package org.zerock.connect.Controller.part2;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.connect.Service.part2.ProgressService;
import org.zerock.connect.entity.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/part2")
public class ProgressController {

    @Autowired
    ProgressService progressService;

    //빈곽 폼 조회
    @GetMapping("/progressForm")
    public String progressForm(HttpSession session) {
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }
        return "/part2/progressForm";
    }

    //검수 예정 품목 리스트 아작스
    @GetMapping("/progressScheduleAjax")
    public String progressScheduleAjax(@RequestParam(value = "searchText", required = false) String searchText,
                                       @RequestParam(value = "searchType", required = false) String searchType,
                                       @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                       @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                       Model model,
                                       @PageableDefault(size = 5, sort = "orderDate", direction = Sort.Direction.ASC) Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        List<Orders> orders = new ArrayList<>();

        //동적 쿼리 만들기 위해 서치타입이 comName이면 comName에다가 넘어온 searchText 값을 넣어서 만들어주기
        String itemCode = "";
        String itemName = "";
        if (searchType.equals("itemCode")) {
            itemCode = searchText;
        }
        if (searchType.equals("itemName")) {
            itemName = searchText;
        }

        if (!Objects.isNull(startDate) && !Objects.isNull(endDate)) {
            orders = progressService.progressScheduleAjax(itemCode, itemName, startDate, endDate); // 날짜가 빈값 아닐때 이쪽
        } else {
            orders = progressService.findProgressScheduleList(itemCode, itemName); //날짜가 비었을때 전체 검색 유도
        }

        // 중복 프로그래스를 리스트에 하나값만 넣기
        orders.forEach(x -> {
            if (!x.getProgresses().isEmpty()) {
                x.setProgresses(Collections.singletonList(x.getProgresses().get(x.getProgresses().size() - 1)));
            }
        });

        System.out.println("orders = " + orders);

        int start = (int) pageable.getOffset();//페이지러블 객체에서 알아서 나오는거 >> 사이즈 5으로 설정 싯 페이지를 1로 넘기면 1페이지에 1~10나옴(size가 10이니까) 2면(11~20)
        int end = Math.min((start + pageable.getPageSize()), orders.size()); // 5을 계산한 구문

        List<Orders> pageContent = orders.subList(start, end); // 데이터가 30개 쌓여있으면  1~10, 11~20, 21~30 이렇게 짤라라
        Page<Orders> progressSchedule = new PageImpl<>(pageContent, pageable, orders.size()); //현재페이지의 보여줄 리스트, 페이지러블 객체, 전체 리스트 개수(예를 들면 글 30개)
        model.addAttribute("progressScheduleList", progressSchedule);//리스트 객체를 페이징 처리 후  보냄

        return "/part2/progressScheduleListAjax";
    }

    //검수 품목 선택 아작스 api
    @GetMapping("/progressChoiceAjax")
    public String progressChoiceAjax(@RequestParam("orderNum") Long orderNum, Model model) {
        Orders orders = progressService.progressChoiceAjax(orderNum);
        Progress progress = progressService.getMaxProgress(orderNum); // max 서비스 추가
        Integer sumCount = progressService.totalAmount(orderNum) == null ? 0 : progressService.totalAmount(orderNum);
        Integer remaining = 0;

        if (orders != null) {
            remaining = orders.getOrderCount() - sumCount;
        }

        model.addAttribute("orders", orders);
        model.addAttribute("progress", progress);
        model.addAttribute("remaining", remaining);
        return "/part2/progressChoiceAjax";
    }

    //검수 저장 API
    @PostMapping("/saveProgress")
    public String saveProgress(@ModelAttribute Progress progress, @ModelAttribute Orders orders, Model model, HttpServletResponse response) throws IOException {

//        //누적합계 계산
//        Integer totalAmount = progressService.totalAmount(orders.getOrderNum()) == null ? 0 : progressService.totalAmount(orders.getOrderNum());
//
//        //진척도 계산
//        double percent = (double) (progress.getProgressAmount() + totalAmount) / orders.getOrderCount() * 100;

        progress.setOrders(orders);
        progress.setProgressPercent(0);
        Progress result = progressService.saveProgress(progress);

//        //입고 예정 테이블에 값을 넣기
//        Receive receive = new Receive();
//
//        receive.setReceiveCount(orders.getOrderCount());
//        receive.setReceiveDate(orders.getReceiveDueDate());
//        receive.setReceiveInfo(orders.getOrderInfo());
//        receive.setReceiveYn("N");
//        receive.setOrders(orders);

//        if (percent == 100) {
//            Receive outcome = progressService.save(receive);
//        } else {
//            System.out.println("receive = " + receive);
//        }


        if (result != null) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('검수계획 등록이 완료되었습니다.');</script>");
            writer.flush();
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('검수계획 등록에 실패 하였습니다.');</script>");
            writer.flush();
        }

        Orders orders2 = progressService.progressChoiceAjax(orders.getOrderNum());
        Progress progress2 = progressService.getMaxProgress(orders.getOrderNum()); // max 서비스 추가
        Integer sumCount = progressService.totalAmount(orders.getOrderNum()) == null ? 0 : progressService.totalAmount(orders.getOrderNum());
        Integer remaining = 0;

        if (orders != null) {
            remaining = orders.getOrderCount() - sumCount;
        }
        model.addAttribute("orders", orders2);
        model.addAttribute("progress", progress2);
        model.addAttribute("remaining", remaining);
        return "/part2/progressChoiceAjax";
    }

    //검수 처리 아작스
    @GetMapping("/progressListAjax")
    public String progressListAjax(@RequestParam("orderNum") Long orderNum, Model model) {
        List<Progress> progress = progressService.progressListAjax(orderNum);
        System.out.println("progress = " + progress);
        // result가 null인것중에 첫번째 행 찾기
        Progress progress1 = progress.stream().filter(x -> x.getProgressResult() == null)
                .findFirst().orElse(null);
        Long progressNum = progress1 == null ? 0 : progress1.getProgressNum();

        Orders orders = progressService.getOrderCount(orderNum);
        Integer sumCount = progressService.totalAmount(orderNum) == null ? 0 : progressService.totalAmount(orderNum);
        Integer remaining = 0;

        if (orders != null) {
            remaining = orders.getOrderCount() - sumCount;
        }
        model.addAttribute("progressList", progress);
        model.addAttribute("remaining", remaining);
        model.addAttribute("progressNum", progressNum);
        return "/part2/progressListAjax";
    }

    //검수 처리 저장
    @PostMapping("/updateProgress")
    public String updateProgress(@ModelAttribute Progress progress, @ModelAttribute Orders orders, Model model, HttpServletResponse response) throws IOException {
        //누적합계 계산
        Integer totalAmount = progressService.totalAmount(orders.getOrderNum()) == null ? 0 : progressService.totalAmount(orders.getOrderNum());

        Orders orders2 = progressService.getOrderCount(orders.getOrderNum());

        //진척도 계산
        double percent = (double) (progress.getProgressAmount() + totalAmount) / orders2.getOrderCount() * 100;

        Integer result = progressService.updateProgress(progress.getProgressAmount(), progress.getProgressResult(), percent, progress.getProgressNum());

        if (percent == 100) {
            // 100% 이면 남은 등록 차수를 지우기
            Integer deleteProgress = progressService.deleteProgress(orders.getOrderNum(), progress.getProgressNum());
        }


        //입고 예정 테이블에 값을 넣기
        Receive receive = new Receive();

        receive.setReceiveCount(orders2.getOrderCount());
        receive.setReceiveDate(orders2.getReceiveDueDate());
        receive.setReceiveInfo(orders2.getOrderInfo());
        receive.setReceiveYn("N");
        receive.setOrders(orders2);

        if (percent == 100) {
            Receive outcome = progressService.save(receive);
        }

        if (result > 0) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('검수처리가 완료되었습니다.');</script>");
            writer.flush();
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('검수처리에 실패 하였습니다.');</script>");
            writer.flush();
        }

        Progress progress2 = progressService.getMaxProgress(orders.getOrderNum()); // max 서비스 추가
        Integer sumCount = progressService.totalAmount(orders.getOrderNum()) == null ? 0 : progressService.totalAmount(orders.getOrderNum());
        Integer remaining = 0;

        if (orders != null) {
            remaining = orders2.getOrderCount() - sumCount;
        }
        List<Progress> progressList = progressService.progressListAjax(orders.getOrderNum());
        // result가 null인것중에 첫번째 행 찾기
        Progress progress1 = progressList.stream().filter(x -> x.getProgressResult() == null)
                .findFirst().orElse(null);
        Long progressNum = progress1 == null ? 0 : progress1.getProgressNum();

        model.addAttribute("orders", orders2);
        model.addAttribute("progress", progress2);
        model.addAttribute("progressNum", progressNum);
        model.addAttribute("remaining", remaining);


        return "/part2/progressListAjax";
    }

    // 검수 리스트 단건 삭제
    @GetMapping("/deleteProgressAjax")
    public String deleteProgressAjax(@RequestParam("progressNum") Long progressNum, @RequestParam("orderNum") Long orderNum, Model model, HttpServletResponse response) throws IOException {
        Integer result = progressService.deleteProgressAjax(progressNum); //삭제 후
        List<Progress> progress = progressService.progressListAjax(orderNum); // 리스트 다시 조회
      // result가 null인것중에 첫번째 행 찾기
        Progress progress1 = progress.stream().filter(x -> x.getProgressResult() == null)
                .findFirst().orElse(null);
        Long progressNum2 = progress1 == null ? 0 : progress1.getProgressNum();
        if (result > 0) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('검수 내역이 삭제 되었습니다..');</script>");
            writer.flush();
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('검수 내역 삭제에 실패 하였습니다.');</script>");
            writer.flush();
        }

        model.addAttribute("progressList", progress); //삭제하고 남은 리스트 보여주기
        model.addAttribute("progressNum", progressNum2);
        return "/part2/progressListAjax";
    }

}
