package org.zerock.connect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.connect.entity.Publish;

@Repository
public interface PublishRepository extends JpaRepository<Publish, Long> {

    boolean existsByReceive_ReceiveNum(Long receiveNum);

    Publish findByInvoiceNumber(Long invoiceNumber);






}
