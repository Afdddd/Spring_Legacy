package com.kh.spring.member.model.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.vo.Member;

@Service
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Autowired
	private MemberDao memberDao;
	

	@Override
	public Member loginMember(Member m) {
		
		/*
		 * 기존 MyBatis 를 연동한 Service 단 코드 흐름
		 * 1. SqlSession 객체 생성
		 * 2. SqlSession 과 전달값을 DAO 단으로 넘기면서 요청 후 결과 받기
		 * 3. 트랜잭션 처리
		 * 4. SqlSession 객체 반납
		 * 5. 결과리턴
		 * 
		 * => Spring 에선 SqlSession 객체 대신 sqlSessionTemplate 객체를 이용
		 *    root-context.xml 에 bean으로 등록해놨음.
		 * => bean으로 등록해뒀기 때문에 sqlSessionTemplate을 직접 생성할 필요없다.
		 *		(@Autowired 를 이용해서 생성 받으면 된다.)   
		 * => 또한 스프링이 sqlSessionTemplate객체를 관리해주기 때문에 내가 직접 sqlSessionTemplate 객체를 반납할 필요도 없다.
		 * 
		 * => 트랜잭션 처리 또한 스프링이 대신 처리 해준다.
		 */
		
		return  memberDao.loginMember(sqlSession,m);
		
		
		
		
	}

	@Override
	public int insertMember(Member m) {
		
		return memberDao.insertMember(sqlSession,m);
	}

	@Override
	public int updateMember(Member m) {
		return memberDao.updateMember(sqlSession,m);
	}

	@Override
	public int deleteMember(String userId) {
		return memberDao.deleteMember(sqlSession,userId);
	}

	@Override
	public int idCheck(String checkId) {
		return memberDao.idCheck(sqlSession,checkId);
	}

}
