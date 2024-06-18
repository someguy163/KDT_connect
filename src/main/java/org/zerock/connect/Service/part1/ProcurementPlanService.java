package org.zerock.connect.Service.part1;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.connect.entity.ProcurementPlan;
import org.zerock.connect.repository.ProcurementPlanRepository;

import java.util.List;

@Service
public class ProcurementPlanService {

    @Autowired
    ProcurementPlanRepository procurementPlanRepository;

    public ProcurementPlan save(ProcurementPlan procurementPlan){
        return procurementPlanRepository.save(procurementPlan);
    }

    public int findAllprcurementPlancount(){
        return procurementPlanRepository.findAllprcurementPlancount();
    }

}
