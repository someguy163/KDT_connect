package org.zerock.connect.Controller.part0;

import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.connect.Controller.part1.ItemController;
import org.zerock.connect.Service.part0.ProductService;
import org.zerock.connect.Service.part1.ItemService;
import org.zerock.connect.entity.Product;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/part0")
@Slf4j //로그찍기
public class ProductController {

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;

    @Autowired
    ItemService itemService;

    //단순 제품 등록 화면폼 조회
    @GetMapping("/productForm")
    public String productForm(){
        return "/part0/productForm";
    }

    //제품 등록 리스트 아작스 + 검색
    @GetMapping("/productListAjax")
    public String productListAjax(@RequestParam("searchText") String searchText, Model model,
                                  @PageableDefault(size = 10, sort = "productId", direction = Sort.Direction.ASC) Pageable pageable) {
        logger.info("searchText : {}", searchText);
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
        List<Product> productList = itemService.productListAjax(searchText);
        int start = (int) pageable.getOffset();//페이지러블 객체에서 알아서 나오는거 >> 사이즈 10으로 설정 싯 페이지를 1로 넘기면 1페이지에 1~10나옴(size가 10이니까) 2면(11~20)
        int end = Math.min((start + pageable.getPageSize()), productList.size()); // 10을 계산한 구문

//        logger.info("productList : {}", productList);

        List<Product> pageContent = productList.subList(start, end);
        Page<Product> product = new PageImpl<>(pageContent, pageable, productList.size());
        model.addAttribute("productList",product);

        return "/part0/productListAjax";
    }



    //신규 제품 등록
    @PostMapping("/saveProduct")
    public String saveProduct(Product product ){
//      인서트

//        System.out.println(AllProductList);
        // LocalDate 타입의 날짜를 문자열로 변환 > "-"를 제거
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = product.getProductStartdate().format(formatter);

        // 제품 ID와 날짜 합치기
        product.setProductId(product.getProductId() + formattedDate);
//        product.setProductId(product.getProductId()+product.getProductStartdate());
        Product InsertProduct = productService.saveProduct(product);
        return "/part0/productForm";
    }

    //제품 코드 중복체크
    @GetMapping("/productIdCheck")
    @ResponseBody
    public Integer productIdCheck(@RequestParam("productId") String productId) {
        System.out.println("productId = " + productId);
        Integer cnt = productService.productIdCheck(productId);
        return cnt; // 1이면 중복이라서 안되고 0이면 등록 가능하게끔 리턴
    }
}
