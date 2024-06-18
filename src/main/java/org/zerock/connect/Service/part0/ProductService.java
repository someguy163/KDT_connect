package org.zerock.connect.Service.part0;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.connect.entity.Product;
import org.zerock.connect.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Product saveProduct(Product product){
        return productRepository.save(product);
    }

    public List<Product> AllProductlist(){
        return productRepository.findAll();
    }

    //제품 코드 중복체크
    public Integer productIdCheck(String productId) {
        return productRepository.productIdCheck(productId);
    }
}
