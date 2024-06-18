package org.zerock.connect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.connect.entity.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Member findByMemberIdAndMemberPw(String memberId, String memberPw);

    Member findByMemberId(String memberId);

}
