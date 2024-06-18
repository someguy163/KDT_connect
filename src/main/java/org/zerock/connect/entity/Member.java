package org.zerock.connect.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DynamicUpdate //값이 있는 것만 업데이트
public class Member {

    @Id
    @Column
    private String memberId;

    @Column
    private String memberDep;

    @Column
    private String memberPw;

    @Column
    private String memberName;

}
