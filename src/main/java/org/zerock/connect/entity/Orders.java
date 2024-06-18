package org.zerock.connect.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@ToString(exclude = "receive")
@DynamicUpdate
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false , length = 20)
    private Long orderNum;

    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY )
    private List<Progress> progresses = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "planNum" ,nullable = false)
    private ProcurementPlan procurementPlan;

    @OneToOne(mappedBy = "orders", fetch = FetchType.LAZY )
    private Receive receive;

    @Column(nullable = false,length = 50)
    private Integer orderCount;

    @Column(length = 50)
    private String orderInfo;

    @Column(nullable = false,length = 5)
    private String orderYn;

    @Column(nullable = false)
    private LocalDate orderDate;

    @Column(nullable = false)
    private LocalDate receiveDueDate;



}
