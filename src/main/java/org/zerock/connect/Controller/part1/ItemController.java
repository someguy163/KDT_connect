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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.connect.Service.DownloadFileService;
import org.zerock.connect.Service.part1.ContractItemService;
import org.zerock.connect.Service.part1.ItemService;
import org.zerock.connect.Service.UploadFileService;
import org.zerock.connect.entity.*;

import java.util.List;

@Controller
@RequestMapping("/part1")
@Slf4j //로그찍기
public class ItemController {

    Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    ItemService itemService;

    @Autowired
    UploadFileService uploadFileService;

    @Autowired
    DownloadFileService downloadFileService;

    @Autowired
    ContractItemService contractItemService;


    //    제품선택화면폼
    @GetMapping("/productList")
    public String productList(HttpSession session){
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }
        return "/part1/productList";
    }

    //제품선택화면 AJAX + 검색
    @GetMapping("/productListAjax")
    public String productListAjax(@RequestParam("searchText") String searchText,Model model,
                                  @PageableDefault(size = 10, sort = "productId", direction = Sort.Direction.ASC) Pageable pageable) {
        logger.info("searchText : {}", searchText);
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
        List<Product> productList = itemService.productListAjax(searchText);
        int start = (int) pageable.getOffset();//페이지러블 객체에서 알아서 나오는거 >> 사이즈 10으로 설정 싯 페이지를 1로 넘기면 1페이지에 1~10나옴(size가 10이니까) 2면(11~20)
        int end = Math.min((start + pageable.getPageSize()), productList.size()); // 10을 계산한 구문

        logger.info("productList : {}", productList);

        List<Product> pageContent = productList.subList(start, end);
        Page<Product> product = new PageImpl<>(pageContent, pageable, productList.size());
        model.addAttribute("productList",product);
        return "/part1/productListAjax";
    }


    //    품목 등록 화면폼
    @GetMapping("/itemForm")
    public String itemForm(Model model, @RequestParam("productId")String productId ,HttpSession session){

        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }
//        분류별 리스트 불러와서 모델로 출력
        List<Unit> unitList = itemService.findUnitList();
        List<Part> partList = itemService.findPartList();
        List<Assy> assyList = itemService.findAssyList();

        model.addAttribute("unitList",unitList);
        model.addAttribute("partList",partList);
        model.addAttribute("assyList",assyList);

//        등록된 품목 리스트불러와서 출력
        List<Item> itemList = itemService.findItemList();
        model.addAttribute("itemList",itemList);

        System.out.println("선택된 productId : " + productId);

        Product product = itemService.findByProductId(productId);

        model.addAttribute("product",product);

        return "/part1/itemForm";
    }

    // 품목 리스트 아작스
    @GetMapping("/itemListAjax")
    public String itemListAjax(@RequestParam("productId") String productId, Model model, @PageableDefault(size = 10, sort = "itemCode", direction = Sort.Direction.ASC) Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
        List<Item> itemList = itemService.itemListAjax(productId);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), itemList.size());

        List<Item> pageContent = itemList.subList(start, end);
        Page<Item> item = new PageImpl<>(pageContent, pageable, itemList.size());
        model.addAttribute("itemList", item);
        return "/part1/itemListAjax";
    }


    // 품목 등록 기능
    @PostMapping("/saveItem")
    public String saveItem(@RequestParam("productId") String productId,
//                           @RequestParam("itemCode") String itemCode,
                           @RequestParam("itemName") String itemName,
                           @RequestParam("itemLength") Integer itemLength,
                           @RequestParam("itemWidth") Integer itemWidth,
                           @RequestParam("itemHeight") Integer itemHeight,
                           @RequestParam("unitCode") String unitCode,
                           @RequestParam("assyCode") String assyCode,
                           @RequestParam("partCode") String partCode,
                           @RequestParam("file") MultipartFile file,
                           @RequestParam("itemMaterial") String itemMaterial,
                           Model model,HttpSession session){
        session.getAttribute("loginedUser");
        Member member= (Member) session.getAttribute("loginedUser");
        if (member==null){
            System.out.println("로그인하세요");
            return "redirect:/Con/login";
        }

        logger.info("productId : {}", productId);
        Product product = new Product(); //객체 생성
        product.setProductId(productId); //객체로 만들어서 조인 값을 저장

        List<Unit> unitList = itemService.findUnitList();
        List<Part> partList = itemService.findPartList();
        List<Assy> assyList = itemService.findAssyList();

        model.addAttribute("unitList",unitList);
        model.addAttribute("partList",partList);
        model.addAttribute("assyList",assyList);


        Unit unit = new Unit();
        unit.setUnitCode(unitCode);

        Assy assy = new Assy();
        assy.setAssyCode(assyCode);

        Part part = new Part();
        part.setPartCode(partCode);


        Item item = new Item(); //객체 생성
        item.setProduct(product);
        item.setUnit(unit);
        item.setAssy(assy);
        item.setPart(part);
        item.setItemCode(item.getProduct().getProductId()+"-"+unitCode+assyCode+partCode);
        item.setItemName(itemName);
        item.setItemLength(itemLength);
        item.setItemWidth(itemWidth);
        item.setItemHeight(itemHeight);
        item.setItemMaterial(itemMaterial);

        //파일쪽 저장
        String itemFile = uploadFileService.upload(file);
        item.setItemFile(itemFile);

        //item 저장
        Item result = itemService.saveItem(item);

        // 상단 제품값 뿌려줘야해서
        Product product1 = itemService.findByProductId(productId);
        model.addAttribute("product",product1);

        return "/part1/itemForm";
    }

    
//    파일 다운로드 기능
    @GetMapping("/download")
    public String downloadFile(@RequestParam("downloadfile") String fileName, HttpServletResponse response) {
        downloadFileService.download(fileName, response);
        System.out.println("파일다운로드");
        return "redirect:/part1/itemForm";
    }

//    @GetMapping("/deleteItem")
//    public String deleteItem(@RequestParam("itemIndex")Long itemIndex){
//        Item selectItem = itemService.findByItemIndex(itemIndex);
////        ContractItem selectContract = contractItemService.findByitemIndex(itemIndex);
//        List<Item> deleteItem = itemService.deleteItemByItemIndex(selectItem);
////        List<ContractItem> deleteContract = contractItemService.deleteContract
//
//
//        return "redirect:/part1/itemForm";
//    }


}
