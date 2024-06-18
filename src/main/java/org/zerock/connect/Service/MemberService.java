package org.zerock.connect.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.connect.entity.Member;
import org.zerock.connect.repository.MemberRepository;


@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    //로그인
    public Member login(String memberId, String memberPw) {
        // MemberRepository를 사용하여 해당 userID로 회원 정보를 조회
        Member member = memberRepository.findByMemberIdAndMemberPw(memberId, memberPw);

        // 회원 정보가 존재하고, 입력된 비밀번호가 회원의 비밀번호와 일치하는지 확인
        if (member != null && member.getMemberPw().equals(memberPw)) {
            return member; // 로그인 성공
        } else {
            return null; // 로그인 실패
        }
    }

    public String getMemberDep(String memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        if (member != null) {
            return member.getMemberDep();
        } else {
            return null; // memberId에 해당하는 회원이 없는 경우
        }
    }

}
