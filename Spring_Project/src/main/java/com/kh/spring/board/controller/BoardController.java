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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
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
	
	@GetMapping("detail.bo")
	public String selectBoard(int bno, Model model) {
		
		
		// bno 에는 상세조회하고자 하는 해당 게시글 번호가 담겨있음
		
		// 1. 해당 게시글의 조회수 증가용 서비스 호출
		int result = boardService.increaseCount(bno);
		
		// 2. 조회수 증가에 성공했다면 해당 게시글 상세 조회 서비스 호출
		if(result>0) {
			
			Board b = boardService.selectBoard(bno);
			model.addAttribute("b" , b);
			
			return "board/boardDetailView";
			
		}else {
			model.addAttribute("errorMsg", "작성 실패!");
			return "common/errorPage";
		}
	}
	
	@PostMapping("delete.bo")
	public String deleteBoard(int bno, String filePath, Model model, HttpSession session) {
		int result = boardService.deleteBoard(bno);
	
		if(result>0) {
			
			// 기존에 첨부파일이 있을경우 서버로부터 해당 첨부 파일 삭제하기
			// filePath 라는 매개변수에는 
			// 기존에 첨부파일이 있었을 경우 수정파일명
			// 기존에 첨부파일이 없었을 경우 "" 이 들어있음
			if(!filePath.equals("")) {
				// 기존에 첨부파일이 있었을 경우
				// => 해당 파일을 삭제 처리
				
				// 해당 파일이 실제 저장되어있는 경로 알아내기
				String realPath = session.getServletContext().getRealPath(filePath);
				
				new File(realPath).delete();
			}
			
			session.setAttribute("alertMsg", "삭제 성공");
			return "redirect:/list.bo";	
		}else {
			model.addAttribute("errorMsg", "작성 실패!");
			return "common/errorPage";
		}
	}
	
	@PostMapping("updateForm.bo")
	public String updateForm(int bno, Model model) {
		Board b = boardService.selectBoard(bno);
		
		model.addAttribute("b",b);
		
		return "board/boardUpdateForm";
		
		
	}
	
	@PostMapping("update.bo")
	public String updateBoard(Board b, MultipartFile reupfile, HttpSession session, Model model) {
		
		// Board b : 제목, 내용
		// MultipartFile reupfile : 새로 넘어온 첨부파일의 정보
		
		if(! reupfile.getOriginalFilename().equals("")){ // 넘어온 파일이 있다면
			if(b.getChangeName() != null) { // 이미 게시판에 파일이 있다면
				String realPath = session.getServletContext().getRealPath(b.getChangeName());				
				new File(realPath).delete(); // 기존 파일 삭제
			}
			
			String changeName = saveFile(reupfile,session); // 파일 업로드
			
			b.setOriginName(reupfile.getOriginalFilename()); // 원본이름 변경
			b.setChangeName("resources/uploadFiles/"+changeName); // 수정이름 변경 
		}
		
		/*
		 * 이 시점 기준으로
		 * b 에 무조건 담겨있는 내용
		 * boardNo, boardTitle, boardContent 
		 * 
		 * 1. 새로 첨부된 파일 X , 기존 첨부차일 X
		 * => orginName : null, changeName : null
		 * 
		 * 2. 새로 첨부된 파일 X, 기존 첨부파일 O
		 * => originName : null, changeName : 기존첨부파일의 수정명
		 * 
		 * 3. 새로 첨부된 파일 o , 기존 첨부파일 X
		 * => originName : 새로 첨부된 파일의 원본명, changeName : 새로 첨부된 파일의 수정명
		 * 
		 * 4. 새로 첨부된 파일 o , 기존 첨부파일 O
		 * => originName : 새로 첨부된 파일의 원본명, changeName : 새로 첨부된 파일의 수정명
		 * 
		 */
		
		int result = boardService.updateBoard(b);
		
		
		if(result>0) {
			
			session.setAttribute("alertMsg", "성공적으로 게시글이 수정되었습니다.");
			return "redirect:/detail.bo?bno="+b.getBoardNo();
			
		}else {
			model.addAttribute("errorMsg", "게시글 수정 실패");
			return "common/erorrPage";
		}

		
		
	}
	

	
	@ResponseBody
	@GetMapping(value="rlist.bo", produces="application/json; charset=UTF-8")
	public String ajaxSelectReplyList(int bno) {
		
		ArrayList<Reply> list = boardService.selectReplyList(bno);
		Gson gson = new Gson();
		return gson.toJson(list);		
	}
	
	
	@ResponseBody
	@GetMapping("rinsert.bo")
	public String ajaxInsertReply(Reply r) {
		
	
		int result = boardService.insertReply(r);
		
		return (result>0)?"success":"fail";
	}
}
	

