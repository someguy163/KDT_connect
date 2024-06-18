package org.zerock.connect.Service.part2;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.connect.repository.OrdersRepository;

@Service
public class OrdersService {

    @Autowired
    OrdersRepository ordersRepository;

    public int findAllorderscount(){
        return ordersRepository.findAllorderscount();
    }
}
