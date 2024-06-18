package org.zerock.connect.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@DynamicUpdate
//@ToString(exclude = "contractItems")
public class Item {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long itemIndex;

    @ManyToOne
    @JoinColumn(name = "productId" ,nullable = false)
    private Product product;


    @ManyToOne
    @JoinColumn(name = "unitCode",nullable = false)
    private  Unit unit;

    @ManyToOne
    @JoinColumn(name = "assyCode" , nullable = false)
    private Assy assy;

    @ManyToOne
    @JoinColumn(name = "partCode" , nullable = false)
    private Part part;

    @Column(nullable = false , length = 30)
    private String itemCode;

    @Column(nullable = false ,length = 100)
    private String itemName;

    @Column(nullable = false , length = 50)
    private int itemLength;

    @Column(nullable = false , length = 50)
    private int itemWidth;

    @Column(nullable = false , length = 50)
    private int itemHeight;

    @Column(nullable = false , length = 50)
    private String itemMaterial;

    @Column(nullable = false , length = 200)
    private String itemFile;

    @OneToMany(mappedBy = "item" )
    private List<ContractItem> contractItems = new ArrayList<>();
}
