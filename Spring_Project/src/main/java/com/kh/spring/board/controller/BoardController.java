package com.kh.spring.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.common.template.Pagination;

@Controller
public class BoardController {


	@Autowired
	private BoardService boardService;
	
	// 메뉴바 클릭시 => list.bo(기본적으로 1번 페이지 요청)
	// 페이징바 클릭시 => list.bo?currentPage=요청하는페이지수

	
	@GetMapping("list.bo")
	public String selectList(@RequestParam(value="cpage" ,defaultValue="1") int currentPage) {
		
		int listCount = boardService.selectListCount();
		Pagination.gerPageInfo(listCount, currentPage, 10, 10);
		
		return "board/boardListView";
	}
	
}
