package org.zerock.connect.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@DynamicUpdate
//@ToString(exclude = "items")
public class Product {

    @Id
    @Column(nullable = false , length = 30)
    private String productId;

    @Column(nullable = false , length = 30)
    private String productName;

    @Column(nullable = false)
    private LocalDate productStartdate;

    @Column(nullable = false)
    private LocalDate productEnddate;

    @OneToMany(mappedBy = "product" )
    private List<Item> items = new ArrayList<>();

}
