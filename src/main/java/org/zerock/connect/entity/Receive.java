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
//@ToString(exclude = "releases")
@DynamicUpdate
public class Receive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 20)
    private Long receiveNum;

    @OneToOne(fetch = FetchType.LAZY )
    @JoinColumn(name="orderNum", nullable = false)
    private Orders orders;

    @OneToMany(mappedBy = "receive", fetch = FetchType.LAZY )
    private List<Releases> releases = new ArrayList<>();

    @OneToOne(mappedBy = "receive", fetch = FetchType.LAZY)
    private Publish publish;

    @Column
    private Integer receiveCount;

    @Column
    private String receiveYn;

    @Column
    private String receiveInfo;

    @Column
    private LocalDate receiveDate;


}
