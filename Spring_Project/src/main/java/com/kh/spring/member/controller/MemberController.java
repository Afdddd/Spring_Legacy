package com.kh.spring.member.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;



@Controller
public class MemberController {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	private final MemberService memberService;
	
	
	
	@Autowired
	public MemberController(MemberService memberService) {
		this.memberService = memberService; 
	}
	
	
	
	
	
//  1. HttpServletRequest 이용 방법
	
//	@RequestMapping(value="login.me")
//	public void loginMember(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
//		request.setCharacterEncoding("UTF-8");
//		
//		String userId = request.getParameter("userId");
//		String userPwd = request.getParameter("userPwd");
//		
//		log.info("userId = {}, userPwd ={}",userId, userPwd);
//		System.out.println("userId = "+userId);
//		System.out.println("userPwd = "+userPwd);
//		
//		
//	}
	
	
//	2. @RequestParam 이용
	
//	@RequestMapping(value="login.me")
//	public void loginMember(@RequestParam("userId") String userId, @RequestParam("userPwd") String userPwd) {	
//		
//		// requestParam 애너테이션에 의해 내부적으로 
//		// String userId = request.getParameter("userId");
//		// String userPwd = request.getParameter("userPwd");
//		log.info("userId={}, userPwd={}", userId, userPwd);		
//	}
	
	
	@RequestMapping("login.me")
	public String loginMember(Member m,Model model, HttpSession session) {
		
		log.info("userId = {}, userPwd = {}",m.getUserId(),m.getUserPwd());
		
		// vo를 service 로 넘기면서 요청 후 결과 받기
		Member loginUser = memberService.loginMember(m);
		// 결과에 따른 응답 뷰 지정
		
		log.info("loginUser = {}",loginUser);
			
		if(loginUser == null) { 
			// 로그인 실패
			// => 에러 문구를 model 객체에 담고 에러페이지로 포워딩
			model.addAttribute("errorMsg","로그인 실패");
			
			//	prefix + common/errorPage + suffix
			// /WEB-INF/views/common/errorPage.jsp
			return "common/errorPage";
			// 내부적으로 DispatcherServlet으로 리턴됨
			// viewResolver에 의해 파일의 경로가 자동완성 된 후에 request.getRequestDispatcher(경로).forward(request,response); 이 실행
									
		}else {
			 // 로그인 성공
			 // => loginUser 를 sessionScope에 담고 메인페이지로 url 재요청
			 
			
			// Spring 에서 Session 객체를 이용하려면  해당 매개변수로 HttpSession 객체를 정의만 하면됨
			session.setAttribute("loginUser", loginUser);
			
			// url 재요청 하고 싶다면 문자열로 리턴
			// 단, redirect: 로 시작하는 문자열로 리턴
			return "redirect:/";	
			// 내부적으로 DispatcherServlet으로 리턴됨
			// response.sendRedirect(url);이 실행
			
			
		}
		

		
		
	}
	
	

	
	
	
}
