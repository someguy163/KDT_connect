package org.zerock.connect.Service.part1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.connect.entity.Company;
import org.zerock.connect.repository.CompanyRepository;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    //신규 업체 등록
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }


    // 등록된 업체 리스트를 보여줌, 검색도 구현(업체명으로 찾고 업체명으로 정렬)
    public List<Company> companyListAjax(String searchText) {
        return companyRepository.findByComNameContainingOrderByComName(searchText); //리스트 타입의 값을 조회할때 기본 메서드
    }

    //업체 사업자번호 중복 체크
    public Integer businessIdCheck(String businessId) {
        return companyRepository.businessIdCheck(businessId);

    }
    public List<Company> findAllCompany(){
        return companyRepository.findAll();
    }

    public Company findByBusinessId(String CompanyId){
        return companyRepository.findByBusinessId(CompanyId);
    }


}
