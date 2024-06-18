package org.zerock.connect.Controller.part1;


import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zerock.connect.Service.part1.CompanyService;
import org.zerock.connect.entity.Company;
import org.springframework.ui.Model;
import org.zerock.connect.entity.Member;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/part1")
@Slf4j //로그찍기
public class CompanyController {

    Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    CompanyService companyService;

    //단순 업체 등록 화면 조회
    @GetMapping("/companyForm")
    public String companyForm(HttpSession session) {
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }
        return "/part1/companyForm";
    }

    //신규 업체 등록
    @PostMapping("/saveCompany")
    public String saveCompany(@ModelAttribute Company company, HttpServletResponse response) throws IOException {
        logger.info("company : {}", company);
        Company result = companyService.saveCompany(company);
        logger.info("result : {}", result); //

        if (result != null) {
            response.setContentType("text/html; charset=UTF-8"); //응답의 content type을 설정, "text/html"은 전송될 데이터의 종류가 HTML임을 나타냄
            PrintWriter writer = response.getWriter(); //이 PrintWriter를 통해 HTML 코드나 다른 텍스트 데이터를 클라이언트로 전송
            writer.println("<script>alert('등록이 완료 되었습니다.');</script>");
            writer.flush();
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('등록에 실패 하였습니다.');</script>");
            writer.flush();
        }

        return "/part1/companyForm";
    }

    //등록된 모든 업체 리스트를 보여주는 아작스 구현
    @GetMapping("/companyListAjax")
    public String companyListAjax(@RequestParam("searchText") String searchText, Model model, @PageableDefault(size = 10, sort = "comName", direction = Sort.Direction.ASC) Pageable pageable) {
        logger.info("searchText : {}", searchText);

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
        List<Company> companyList = companyService.companyListAjax(searchText);
        int start = (int) pageable.getOffset();//페이지러블 객체에서 알아서 나오는거 >> 사이즈 10으로 설정 싯 페이지를 1로 넘기면 1페이지에 1~10나옴(size가 10이니까) 2면(11~20)
        int end = Math.min((start + pageable.getPageSize()), companyList.size()); // 10을 계산한 구문

//        logger.info("companyList : {}", companyList);

        List<Company> pageContent = companyList.subList(start, end); // 데이터가 30개 쌓여있으면  1~10, 11~20, 21~30 이렇게 짤라라
        Page<Company> company = new PageImpl<>(pageContent, pageable, companyList.size()); //현재페이지의 보여줄 리스트, 페이지러블 객체, 전체 리스트 개수(예를 들면 글 30개)
        model.addAttribute("companyList", company);//리스트 객체를 페이징 처리 후  보냄
        return "/part1/companyListAjax";
    }


    //업체 등록 중복체크
    @GetMapping("/businessIdCheck")
    @ResponseBody
    public Integer businessIdCheck(@RequestParam("businessId") String businessId) {
        Integer cnt = companyService.businessIdCheck(businessId);
        return cnt; // 1이면 중복이라서 안되고 0이면 등록 가능하게끔 리턴
    }








}
