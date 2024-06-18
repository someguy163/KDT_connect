package org.zerock.connect.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "items")
@DynamicUpdate
public class Assy {

    @Id
    @Column(nullable = false ,length = 10)
    private String assyCode;


    @Column(nullable = false ,length = 20)
    private String assyName;


    @OneToMany(mappedBy = "assy")
    private List<Item> items = new ArrayList<>();
}
