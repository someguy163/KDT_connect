package org.zerock.connect.Service.part1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.connect.entity.ContractItem;
import org.zerock.connect.entity.Item;
import org.zerock.connect.repository.ContractItemRepository;

import java.util.List;

@Service
public class ContractItemService {

    @Autowired
    ContractItemRepository contractItemRepository;

    public List<ContractItem> findAllContractItemList(){
        return contractItemRepository.findAll();
    }

    public List<ContractItem> contractitemListAjax() {
        return contractItemRepository.findAll();
    }

    public ContractItem saveContractItem(ContractItem contractItem){
        return contractItemRepository.save(contractItem);
    }

    public List<ContractItem> ContractItemList(){
        return contractItemRepository.ContractItemList();
    }

    public List<ContractItem> selectByConitemNo(Long conitemNo){
        return contractItemRepository.selectByConitemNo(conitemNo);
    }

    public ContractItem findByConitemNo(Long conitemNo){
        return contractItemRepository.findByConitemNo(conitemNo);
    }
    public List<ContractItem> selectByConitemName(String itemName){
        return contractItemRepository.selectByConitemName(itemName);
    }

    public int countContractItemcount(){
        return contractItemRepository.countContractItemcount();
    }
}

