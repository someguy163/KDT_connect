package org.zerock.connect.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zerock.connect.entity.Company;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    List<Company> findByComNameContainingOrderByComName(String searchText);


    //사업자번호 중복체크
    @Query("select count(*) from Company  where businessId =:businessId")
    Integer businessIdCheck(@Param("businessId") String businessId);


    @Query("select C from Company C where C.businessId =:CompanyId")
    Company selectCompany(@Param("CompanyId") String CompanyId);

//    Company findByBusinessId(String businessId);

    Company findByBusinessId(String businessId);
//    Company findByBusinessId(String businessId);


}


