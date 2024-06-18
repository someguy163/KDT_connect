package org.zerock.connect.Controller.part3;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.connect.Service.part3.ReceiveService;
import org.zerock.connect.entity.Member;
import org.zerock.connect.entity.Receive;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/part3")
public class ReceiveController {

    @Autowired
    ReceiveService receiveService;

    //단순 입고 화면 페이지 조회
    @GetMapping("/receiveList")
    public String receiveListForm(HttpSession session) {
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }
        return "/part3/receiveList";
    }


    // 입고 예정 품목 리스트 아작스
    @GetMapping("/getNoReceiveListAjax")
    public String getNoReceiveListAjax(@RequestParam(value = "searchText", required = false) String searchText,
                                     @RequestParam(value = "searchType", required = false) String searchType,
                                     Model model,
                                     @PageableDefault(size = 5, sort = "receiveNum", direction = Sort.Direction.ASC) Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());


        //동적 쿼리 만들기 위해 서치타입이 comName이면 comName에다가 넘어온 searchText 값을 넣어서 만들어주기
        String itemCode = "";
        String itemName = "";
        if (searchType.equals("itemCode")) {
            itemCode = searchText;
        }
        if (searchType.equals("itemName")) {
            itemName = searchText;
        }

        List<Receive> receive = receiveService.getReceiveListAjax(itemCode, itemName, "N");

        int start = (int) pageable.getOffset();//페이지러블 객체에서 알아서 나오는거 >> 사이즈 5으로 설정 싯 페이지를 1로 넘기면 1페이지에 1~10나옴(size가 10이니까) 2면(11~20)
        int end = Math.min((start + pageable.getPageSize()), receive.size()); // 5을 계산한 구문

        List<Receive> pageContent = receive.subList(start, end); // 데이터가 30개 쌓여있으면  1~10, 11~20, 21~30 이렇게 짤라라
        Page<Receive> receiveList = new PageImpl<>(pageContent, pageable, receive.size()); //현재페이지의 보여줄 리스트, 페이지러블 객체, 전체 리스트 개수(예를 들면 글 30개)
        model.addAttribute("noReceiveList", receiveList);//리스트 객체를 페이징 처리 후  보냄

        return "/part3/noReceiveListAjax"; // 해당 뷰로 이동
    }

    // 입고 완료 품목 리스트 아작스
    @GetMapping("/getYesReceiveListAjax")
    public String getYesReceiveListAjax(@RequestParam(value = "searchText", required = false) String searchText,
                                       @RequestParam(value = "searchType", required = false) String searchType,
                                       Model model,
                                       @PageableDefault(size = 5, sort = "receiveNum", direction = Sort.Direction.ASC) Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());


        //동적 쿼리 만들기 위해 서치타입이 comName이면 comName에다가 넘어온 searchText 값을 넣어서 만들어주기
        String itemCode = "";
        String itemName = "";
        if (searchType.equals("itemCode")) {
            itemCode = searchText;
        }
        if (searchType.equals("itemName")) {
            itemName = searchText;
        }

        List<Receive> receive = receiveService.getReceiveListAjax(itemCode, itemName, "Y");
        System.out.println("itemName = " + itemName);
        System.out.println("itemCode = " + itemCode);
        System.out.println("receive = " + receive);

        int start = (int) pageable.getOffset();//페이지러블 객체에서 알아서 나오는거 >> 사이즈 5으로 설정 싯 페이지를 1로 넘기면 1페이지에 1~10나옴(size가 10이니까) 2면(11~20)
        int end = Math.min((start + pageable.getPageSize()), receive.size()); // 5을 계산한 구문

        List<Receive> pageContent = receive.subList(start, end); // 데이터가 30개 쌓여있으면  1~10, 11~20, 21~30 이렇게 짤라라
        Page<Receive> receiveList = new PageImpl<>(pageContent, pageable, receive.size()); //현재페이지의 보여줄 리스트, 페이지러블 객체, 전체 리스트 개수(예를 들면 글 30개)
        model.addAttribute("yesReceiveList", receiveList);//리스트 객체를 페이징 처리 후  보냄

        return "/part3/yesReceiveListAjax"; // 해당 뷰로 이동
    }

    // 입고 버튼 api
    @GetMapping("/receive")
    public String receive(@RequestParam("receiveNum") Long receiveNum, HttpServletResponse response) throws IOException {
        Integer result = receiveService.receive(receiveNum);

        if (result > 0) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('입고 처리가 완료되었습니다.');</script>");
            writer.flush();
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('입고 처리에 실패 하였습니다.');</script>");
            writer.flush();
        }
        return "/part3/receiveList";
    }
}