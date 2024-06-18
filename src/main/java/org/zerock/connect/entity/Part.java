package org.zerock.connect.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@ToString(exclude = "items")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@DynamicUpdate
public class Part {

    @Id
    @Column(nullable = false , length = 10)
    private String  partCode;

    @Column(nullable = false , length = 20)
    private String partName;

    @OneToMany(mappedBy = "part")
    private List<Item> items = new ArrayList<>();



}
