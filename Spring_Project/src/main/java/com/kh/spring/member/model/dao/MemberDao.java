package com.kh.spring.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.spring.member.model.vo.Member;

@Repository // 저장소 (dao는 데이터 입출력이 일어나는 부분)
// Repository 타입의 애너테이션을 붙이면
// 빈 스캐닝을 통해 DAO 형식의 bean으로 등록됨
public class MemberDao {
	
	public Member loginMember(SqlSessionTemplate sqlSession, Member m) {
		
		return sqlSession.selectOne("memberMapper.loginMember",m);
	}

}
