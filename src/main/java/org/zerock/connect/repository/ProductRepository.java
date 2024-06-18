package org.zerock.connect.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zerock.connect.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Product findByProductId(String productId);

    List<Product> findByProductIdContainingOrderByProductId(String searchText);

    //제품 코드 중복체크
    @Query("select count(*) from Product where productId =:productId")
    Integer productIdCheck(@Param("productId") String productId);
}
