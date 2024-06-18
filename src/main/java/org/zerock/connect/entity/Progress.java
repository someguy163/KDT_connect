package org.zerock.connect.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

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
@ToString(exclude = "orders")
@DynamicUpdate
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long progressNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="orderNum", nullable = false)
    private Orders orders;

    @Column
    private Integer progressAmount;

    @Column(nullable = false)
    private LocalDate progressDate;

    @Column(nullable = false)
    private Integer progressCount;

    @Column(nullable = false)
    private Integer progressPercent;

    @Column
    private String progressResult;

}
