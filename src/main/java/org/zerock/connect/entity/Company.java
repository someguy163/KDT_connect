package org.zerock.connect.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.Modifying;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "contractItems")
@DynamicUpdate
public class Company {
    @Id
    @Column(nullable = false ,length = 50)
    private String businessId;

    @Column(nullable = false ,length = 50)
    private String comName;

    @Column(nullable = false ,length = 100)
    private String comAdd;

    @Column(nullable = false ,length = 30)
    private String comManager;

    @Column(nullable = false ,length = 30)
    private String comPhone;

    @Column(nullable = false ,length = 50)
    private String comEmail;

    @Column(nullable = false ,length = 50)
    private String comAccount;

    @OneToMany(mappedBy = "company")
    private List<ContractItem> contractItems = new ArrayList<>();

}
