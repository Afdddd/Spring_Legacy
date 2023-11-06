package com.kh.spring.member.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
	
	
//	@RequestMapping("login.me")
//	public String loginMember(Member m,Model model, HttpSession session) {
//		
//		log.info("userId = {}, userPwd = {}",m.getUserId(),m.getUserPwd());
//		
//		// vo를 service 로 넘기면서 요청 후 결과 받기
//		Member loginUser = memberService.loginMember(m);
//		// 결과에 따른 응답 뷰 지정
//		
//		log.info("loginUser = {}",loginUser);
//			
//		if(loginUser == null) { 
//			// 로그인 실패
//			// => 에러 문구를 model 객체에 담고 에러페이지로 포워딩
//			model.addAttribute("errorMsg","로그인 실패");
//			
//			//	prefix + common/errorPage + suffix
//			// /WEB-INF/views/common/errorPage.jsp
//			return "common/errorPage";
//			// 내부적으로 DispatcherServlet으로 리턴됨
//			// viewResolver에 의해 파일의 경로가 자동완성 된 후에 request.getRequestDispatcher(경로).forward(request,response); 이 실행
//									
//		}else {
//			 // 로그인 성공
//			 // => loginUser 를 sessionScope에 담고 메인페이지로 url 재요청
//			 
//			
//			// Spring 에서 Session 객체를 이용하려면  해당 매개변수로 HttpSession 객체를 정의만 하면됨
//			session.setAttribute("loginUser", loginUser);
//			
//			// url 재요청 하고 싶다면 문자열로 리턴
//			// 단, redirect: 로 시작하는 문자열로 리턴
//			return "redirect:/";	
//			// 내부적으로 DispatcherServlet으로 리턴됨
//			// response.sendRedirect(url);이 실행
//			
//			
//		}
	
		/*
		 * 2. 스프링에서 제공하는 ModelAndBiew 객체를 사용하는 방법
		 * Model 객체는 데이터를 Key + value 세트로 담을 수 있는 requestScope 공간이라고 한다면, View는 응답뷰에 대한 정보를 담을 수 있는 공간.
		 * => Model 객체와 View객체가 결합된 형태의 객체
		 *    단, Model 객체는 단독으로 존재가 가능하지만 View 객체는 단독으로 존재하지 않는다.
		 * => String 형태가 아닌 ModelAndView 객체를 리턴해야 한다. 
		 * 
		 */
	
		@RequestMapping("login.me") // value 속성은 생략가능
		public ModelAndView loginMember(Member m, ModelAndView mv, HttpSession session) {
			
			// 전달 받은 m을 service로 요청 후 결과 받기
			Member loginUser = memberService.loginMember(m);
			
			// 결과에 따른 응답뷰 지정
			if(loginUser == null) {
				// 로그인 실패
				// => 에러문구를 requestScope에 담고 에러페이지로 포워딩
				
				// ModelAndView 객체에 응답데이터 담기
				// addObject 메서드를 이용(키 + 밸류)
				// [표현법]
				// mv.addObject("키",밸류);
				mv.addObject("errorMsg","로그인 실패");
				
				// ModelAndView 객체로 응답페이지를 포워딩하기
				// setViewName 메서드 이용 (응답페이지 경로)
				// [표현법]
				// mv.setViewName("응답페이지 경로")
				// => 포워딩 시 주소를 자동완성시켜주는 View Resolver가 
				//    ModelAndView 방식도 주소를 자동완성 해주기 때문에 접두어와 접미어를 뺀 나머지 부분만 명시해야함.
				
				// 에러페이지 경로 
				// /WEB-INF/views/common/errorPage.jsp
				mv.setViewName("common/errorPage");
				
			}else{
				// 로그인 성공
				// => loginUser를 sessionScope에 담고 
				// 메인페이지로 url 재요청
				session.setAttribute("loginUser",loginUser);
				
				// ModelAndView 객체로 url 재요청
				// 마찬가지로 setViewName 메서드를 씀
				// url 재요청 시에는 "redirect:"으로 시작하는 문자열로 지정
				mv.setViewName("redirect:/");	
			}
			
			// 이 시점 기준으로 mv에는 각각 성공, 실패 시 응답 정보들이 들어있음
			return mv;
			
	}
		
	@RequestMapping("logout.me")
	public String logoutMember(HttpSession session) {
		
		//세션 무효화
		session.invalidate();
		
		//메인 페이지 url 재요청
		return "redirect:/";
	}
	
	@RequestMapping("enrollForm.me")
	public String enrollFormMember() {		
	
		// 단순히 회원가입 페이지만 포워딩
		return "enrollFormMember";
	}
	
	
	
	

	
	
	
}
