package org.zerock.connect.Controller.part1;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.connect.Service.DownloadFileService;
import org.zerock.connect.Service.UploadFileService;
import org.zerock.connect.Service.part1.CompanyService;
import org.zerock.connect.Service.part1.ContractItemService;
import org.zerock.connect.Service.part1.ItemService;
import org.zerock.connect.entity.Company;
import org.zerock.connect.entity.ContractItem;
import org.zerock.connect.entity.Item;
import org.zerock.connect.entity.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/part1")
public class ConatractItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    CompanyService companyService;

    @Autowired
    ContractItemService contractItemService;

    @Autowired
    UploadFileService uploadFileService;

    @Autowired
    DownloadFileService downloadFileService;

    @GetMapping("/contractItem")
    public String contractItem(Company company , Item item , Model model , @PageableDefault(size = 5, sort = "conitemNo", direction = Sort.Direction.ASC) Pageable pageable, HttpSession session){
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }

//      모든 업체 리스트 출력
        List<Company> AllCompany = companyService.findAllCompany();
        model.addAttribute("AllCompany",AllCompany);

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
        List<ContractItem> contractitemList = contractItemService.findAllContractItemList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), contractitemList.size());

        List<ContractItem> pageContent = contractitemList.subList(start, end);
        Page<ContractItem> contractItems = new PageImpl<>(pageContent, pageable, contractitemList.size());
        model.addAttribute("contractitemList", contractItems);

        return "/part1/contractForm";
    }
    

    @GetMapping("/NocontractItem")
    public String NocontractItem(Company company , Item item , Model model ,@PageableDefault(size = 5, sort = "conitemNo", direction = Sort.Direction.ASC) Pageable pageable ,HttpSession session){
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }
//      모든 업체 리스트 출력
        List<Company> AllCompany = companyService.findAllCompany();
        model.addAttribute("AllCompany",AllCompany);
//       페이징 처리
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
        List<Item> NocontractItem = itemService.NocontractItem();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), NocontractItem.size());

        List<Item> pageContent = NocontractItem.subList(start, end);
        Page<Item> NocontractItems = new PageImpl<>(pageContent, pageable, NocontractItem.size());
        model.addAttribute("NocontractItems", NocontractItems);
        return "/part1/NocontractForm";
    }

//   제품선택하기
    @GetMapping("selectContractItem")
    public String selectContractItem(@RequestParam("selectItemIndex")Long itemIndex , Model model ,@PageableDefault(size = 5, sort = "conitemNo", direction = Sort.Direction.ASC) Pageable pageable ,HttpSession session){
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }
        System.out.println(itemIndex);

//선택한 item정보 모델로 출력하기
        Item selectItem = itemService.findByItemIndex(itemIndex);
        model.addAttribute("selectItem",selectItem);

        List<Company> AllCompany = companyService.findAllCompany();
        model.addAttribute("AllCompany",AllCompany);
        System.out.println(AllCompany);


        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
        List<Item> NocontractItem = itemService.NocontractItem();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), NocontractItem.size());

        List<Item> pageContent = NocontractItem.subList(start, end);
        Page<Item> NocontractItems = new PageImpl<>(pageContent, pageable, NocontractItem.size());
        model.addAttribute("NocontractItems", NocontractItems);

        return "/part1/selectcontractForm";
    }

    @PostMapping("/saveContractitem")
    public String saveContractitem(@RequestParam("itemIndex") Long itemIndex , @RequestParam(value = "CompanyId") String businessId , ContractItem contractItem , @RequestParam("file")MultipartFile file){

        //        파라미터로 받아온 itemIndex 를 리파지토리에서 조회한후 저장
        Item selectItem = itemService.findByItemIndex(itemIndex);
        contractItem.setItem(selectItem);

//        Company selectCompany = companyService.findByBusinessId(businessId);
//        Company company = new Company();
//        company.setBusinessId(businessId);
//        contractItem.setCompany(company);

        //        파라미터로 받아온 businessId 를 리파지토리에서 조회한후 저장
        Company selectCompany = companyService.findByBusinessId(businessId);
        contractItem.setCompany(selectCompany);


        //        파라미터로 받아온 file 를 파일업로드 서비스를 사용해서 직접 contractitem에 저장하기
        String savedFile = uploadFileService.upload(file);
        contractItem.setContractFile(savedFile);

//        contractitem의 계약시간을 현재시간으로 강제주입
        contractItem.setContractDate(LocalDate.now());
        contractItem.setContractYn("1");
        ContractItem resultContract = contractItemService.saveContractItem(contractItem);


        return "redirect:/part1/contractItem";
    }

    @GetMapping("/Condownload")
    public String downloadFile(@RequestParam("downloadfile") String fileName, HttpServletResponse response) {
        downloadFileService.download(fileName, response);
        System.out.println("파일다운로드");
        return "redirect:/part1/contractItem";
    }

}
