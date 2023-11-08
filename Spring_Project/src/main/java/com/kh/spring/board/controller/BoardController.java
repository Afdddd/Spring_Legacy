package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.common.model.vo.PageInfo;
import com.kh.spring.common.template.Pagination;

@Controller
public class BoardController {


	@Autowired
	private BoardService boardService;
	
	// 메뉴바 클릭시 => list.bo(기본적으로 1번 페이지 요청)
	// 페이징바 클릭시 => list.bo?currentPage=요청하는페이지수

	
	@GetMapping("list.bo")
	public String selectList(@RequestParam(value="cpage" ,defaultValue="1") int currentPage, Model model) {
		
		int listCount = boardService.selectListCount();
		int pageLimit = 5;
		int boardLimit = 5;
		PageInfo pi = Pagination.gerPageInfo(listCount, currentPage, pageLimit, boardLimit);
		
		ArrayList<Board> list = boardService.selectList(pi);
		
		model.addAttribute("list", list);
		model.addAttribute("pi", pi);
		
		return "board/boardListView";
	}
	
	
	@GetMapping("enrollForm.bo")
	public String enrollForm() {
		return "board/boardEnrollForm";
	}
	
	@PostMapping("insert.bo")
	public String insertBoard(Board b, MultipartFile upfile, HttpSession session, Model model) {
		
		// Spring 에서 요청 시 넘어온 첨부파일을 
		// MultipartFile 타입으로 한번에 전달받을 수 있다.
		// 단 첨부파일을 선택하든 안했든 간에 생성된 객체다.
		// 첨부파일이 넘어왔을 경우 fileName 속성에 원본파일명이 담겨있음
		// 첨부파일이 넘어오지 않았을 경우 fileName 속성이 비어있다.
		
		// 참고) 만약, 게시글 한 개당 첨부파일을 다중으로 넘기고 싶을 경우
		// 		jsp 에서 <input type="file" multiple name="">
		// 		multiple 속성을 붙인 후 
		// 		Controller 단의 메서드에서 
		// 		매개변수로 MultipartFile[] 키값 또는
		//		ArrayList<MultipartFile> 키값으로 받을 수 있음
		//		또는 jsp 에서 <input type="file" name="키값"> * 여러개 배치 가능
		// => 이 경우에는 세미프로젝트때 처럼 Board 테이블과 Attachment 테이블을 따로 쪼개야함
		
		// 전달된 파일이 있을 경우
		// => 파일명을 수정 후 서버로 업로드
		if(!(upfile.getOriginalFilename()).equals("")) {
			// 첨부파일 이름이 있다면 넘어온것
			
			String changeName = saveFile(upfile,session);
			
			
			// 8. 원본명, 서버에 업로드된 수정파일명을 Board b 에 이어서 담기
			b.setOriginName(upfile.getOriginalFilename());
			b.setChangeName("resources/uploadFiles/"+changeName);
		}
			
		// 이 시점 기준으로
		// 넘어온 첨부파일이 있었을 경우 (if문을 거쳤기 때문)
		// Board b : 제목, 작성자아이디, 내용, 원본파일명, 수정파일명
		// 넘어온 첨부파일이 없었을 경우 (if문을 거치지 않았기 때문)
		// Board b : 제목, 작성자아이디, 내용		
		
		int result = boardService.insertBoard(b);
		if(result > 0) {
			
			session.setAttribute("alertMsg","작성되었습니다.");
			return "redirect:/list.bo";
		}else {
			
			model.addAttribute("errorMsg", "작성 실패!");
			return "common/errorPage";
		}
		
		
		
	}
	
	
	
	// 현재 넘어온 첨부파일 그 자체를 서버의 폴더에 저장시키는 역할
	public String saveFile(MultipartFile upfile, HttpSession session) {
		// 파일명 수정 작업 진행 후 서버로 업로드 시키기
		// 예) "bono.jpg => 2023110810223012345.jpg
		// 1. 원본파일명 뽑아오기
		String originName = upfile.getOriginalFilename();
		
		// 2. 시간 형식을 문자열로 뽑아내기
		// "2023110810252230" (년월일시분초)
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		// 3. 뒤에 붙을 5가지 랜덤수 뽑기(10000~99999)
		int ranNum = (int)(Math.random()*90000+10000);
		
		// 4. 원본파일명으로 부터 확장자명을 뽑기
		String ext =originName.substring(originName.lastIndexOf(".")); 
		
		// 5. 모두 이어 붙이기
		String changeName = currentTime + ranNum + ext;
		
		// 6. 업로드 하고자 하는 물리적인 경로 알아내기
		String savePath = session.getServletContext().getRealPath("/resources/uploadFiles/");
		
		// 7. 경로와 수정 파일명을 합체 후 파일을 업로드 해주기
		try {
			upfile.transferTo(new File(savePath + changeName));
		} catch (IllegalStateException | IOException e) {				
			e.printStackTrace();
		}
		
		//8 만들어진 수정명 반환
		return changeName;
	}
	
}
