package org.zerock.connect.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.mapping.ToOne;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@ToString(exclude = "orders")
@DynamicUpdate
public class ProcurementPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false , length = 20)
    private Long planNum;

    @ManyToOne
    @JoinColumn(name = "conitemNo" , nullable = false)
    private ContractItem contractItem;

    @Column(nullable = false)
    private LocalDate planDate;

    @Column(nullable = false)
    private int planCount;

    @OneToOne(mappedBy = "procurementPlan" )
    private Orders orders;
}
