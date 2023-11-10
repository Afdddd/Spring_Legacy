package com.kh.spring.board.model.service;

import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.board.model.dao.BoardDao;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.model.vo.PageInfo;

@Service
@EnableTransactionManagement
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardDao boardDao;
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Override
	public int selectListCount() {
		return boardDao.selectListCount(sqlSession);
	}

	@Override
	public ArrayList<Board> selectList(PageInfo pi) {
		return boardDao.selectList(sqlSession, pi);
	}

	@Override
	@Transactional
	public int insertBoard(Board b) {
		return boardDao.insertBoard(sqlSession, b);
	}

	@Override
	@Transactional
	public int increaseCount(int boardNo) {
		return boardDao.increaseCount(sqlSession,boardNo);
	}

	@Override
	public Board selectBoard(int boardNo) {
		return boardDao.selectBoard(sqlSession,boardNo);
	}

	@Override
	@Transactional
	public int deleteBoard(int boardNo) {
		return boardDao.deleteBoard(sqlSession,boardNo);
	}

	@Override
	@Transactional
	public int updateBoard(Board b) {
		return boardDao.updateBoard(sqlSession,b);
	}
	

	@Override
	public ArrayList<Reply> selectReplyList(int boardno) {
		return boardDao.selectReplyList(sqlSession,boardno);
	}

	@Override
	public int insertReply(Reply r) {
		return boardDao.insertReply(sqlSession,r);
	}
	
	

}
