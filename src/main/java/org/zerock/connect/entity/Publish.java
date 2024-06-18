package org.zerock.connect.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@ToString
@DynamicUpdate
public class Publish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 20)
    private Long invoiceNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiveNum", nullable = false)
    private Receive receive;

    @Column
    private LocalDate invoiceDate;

    @Column
    private String invoiceMemo;

    @Column
    private String publisher;

}
