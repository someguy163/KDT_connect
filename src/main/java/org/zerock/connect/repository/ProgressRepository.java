package org.zerock.connect.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zerock.connect.entity.Orders;
import org.zerock.connect.entity.Progress;

import java.util.List;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    //진척 검수 삭제
    @Modifying
    @Query("delete from Progress p where p.orders.orderNum =:orderNum")
    Integer deletePlan(@Param("orderNum") Long orderNum);

    //차수 관련 쿼리
    @Query("select p from Progress p where p.orders.orderNum =:orderNum order by p.progressCount DESC limit 1")
    Progress getMaxProgress(@Param("orderNum") Long orderNum);

    @Query("select p from Progress p where p.orders.orderNum =:orderNum")
    List<Progress> progressListAjax(@Param("orderNum") Long orderNum);

    @Query("select sum(p.progressAmount) from Progress p where p.orders.orderNum =:orderNum")
    Integer totalAmount(@Param("orderNum") Long orderNum);

    Integer deleteByProgressNum(Long progressNum);

    //검수 처리 저장
    @Modifying
    @Query("update Progress p set p.progressAmount =:progressAmount, p.progressResult =:progressResult,  p.progressPercent =:percent where p.progressNum =:progressNum ")
    Integer updateProgress(@Param("progressAmount") Integer progressAmount, @Param("progressResult") String progressResult, @Param("percent") Integer percent, @Param("progressNum") Long progressNum);

    // 100% 이면 남은 등록 차수를 지우기
    @Modifying
    @Query("delete from Progress p where p.orders.orderNum =:orderNum and p.progressNum >:progressNum")
    Integer deleteProgress(@Param("orderNum") Long orderNum, @Param("progressNum") Long progressNum);
}
