package com.kh.spring.board.model.service;

import java.util.ArrayList;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.model.vo.PageInfo;

public interface BoardService {
	
	// 게시글 목록 조회 서비스 + 페이징 처리
	// 게시글의 총 갯수 조회
	int selectListCount();
	
	// 게시글 리스트 조회
	ArrayList<Board> selectList(PageInfo pi);

	// 게시글 작성하기
	int insertBoard(Board b);
	
	// 게시글 상세 조회 서비스
	// 게시글 조회수 증가
	int increaseCount(int boardNo);
	
	// 게시글 상세조회
	Board selectBoard(int boardNo);
	
	// 게시글 삭제 서비스
	int deleteBoard(int board);
	
	// 게시글 수정 서비스
	int updateBoard(Board b);
	
	// 댓글 리스트 조회 서비스(ajax)
	ArrayList<Reply> selectReplyList(int boardno);
	
	// 댓글 작성 서비스
	int insertReply(Reply r);
	
	// 탑앤분석
	ArrayList<Board> selectTopBoardList();
}
