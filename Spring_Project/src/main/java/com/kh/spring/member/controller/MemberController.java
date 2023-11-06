package com.kh.spring.member.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;



@Controller
public class MemberController {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	private final MemberService memberService;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
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
			
			// 암호화 작업 후
			// Member m 의 userId 필드 : 사용자가 입력한 아이디
			// 			  userPWd 필드 : 사용자가 입력한 비번 평문 
			Member loginUser = memberService.loginMember(m);
			
			// 아이디만으로 회원을 조회할 수 있게끔 쿼리문을 수정함
			// loginUser : 오로지 아이디만을 가지고 조회된 회원
			// loginUser 의 userPwd 필드
			// : DB에 실제 기록된 암호화된 해당 회원 비번
			
			// m의 userPwd 필드값 (평문)과 
			// loginUser 의 userPwd 필드값(암호문)을 대조
			
			// 로직을 변경한 이유
			// BCrypt 방식은 매번 평문에 랜덤값을 심어서 암호화를 해줌
			// 매번 같은평문이어도 다른 암호문 결과가 나올 수 밖에 없음
			// DB로부터 Unique 제액이 걸린 Id값을 기준으로 일치하는 회워의 정보를 모두 갖고왔거,(암호화된 비번 포함)
			// 암호화된 비번과 평문비번을 대조하여 일치하면 통고ㅓ
			
			//BcryptPasswordEncoder 객체에서 제공하는 mathes 메서드
			// [표현법]
			// encoder.matches("ㅍ평문","암호문:) : boolean
			
			// 로그인 성공의 조건
			// 1. loginUser 객체가 null 이 아닐것 (아이디에대한 조건)
			// 2. matches 메서드의 결과가 true 일것 (비번에 대한 조건)
			// => AND 연산자로 조건 엮기
			
			if(loginUser != null && bcryptPasswordEncoder.matches(m.getUserPwd(), loginUser.getUserPwd())) {
				// 로그인 성공												
				// => loginUser를 sessionScope에 담고 
				// 메인페이지로 url 재요청
				session.setAttribute("loginUser",loginUser);
				session.setAttribute("alertMsg", "로그인에 성공했습니다!");
				
				
				// ModelAndView 객체로 url 재요청
				// 마찬가지로 setViewName 메서드를 씀
				// url 재요청 시에는 "redirect:"으로 시작하는 문자열로 지정
				mv.setViewName("redirect:/");	
				
			}else {
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
			}
			
			
			
			
			
			
			/* 암호화 작업 전
		
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
			
			 * 
			 */
			
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
	public String memberEnrollForm() {		
	
		// 단순히 회원가입 페이지만 포워딩
		return "member/memberEnrollForm";
	}
	
	@RequestMapping("insert.me")
	public ModelAndView insertMember(Member m, ModelAndView mv, HttpSession session) {
		// 이슈1)encoding 처리
		// 스프링에서 제공하는 인코딩 필터 사용
		// view --> Filter --> DispatcherServlet -> controller 순
		// 					  (요청 시 전달값을 뽑아서 매개변수로 전달)
		// => web.xml 파일에 스프링에서 제공하는 인코딩 필터를 등록
		// 
		
		// 이슈2)
		// 필수 입력사항이 아닌 것을 입력하지 않았을 경우
		// 400 Bad Request Error 발생
		// => int 자료형인 age 라는 필드에 빈 문자열 값이 넘어와 자료형이 맞지 않는 문제 발생
		
		// 해결방법1. HttpServletRequest 로 직접 코드 다 쓰기
		
		// 해결방법2. Member 클래스의 age필드를 int형에서 String 형으로 변경하기
		
		// 이슈3) 
		// 비밀번호가 사용자가 입력한 있는 그대로의 "평문" 으로 노출됨
		// => Bcrypt 방식의 암호화를 통해 "암호문"으로 변경
		//    (매번 다른 랜덤값 == salt값을 평문에 붙여 암호화를 하는 기법, 단방향성을 가짐 == 복호화 과정X)
		// 1. 스프링 시큐리티 모듈에서 제공(pom.xml에 라이브러리 추가)
		// 2. BCryptPasswordEncoder 클래스를 bean으로 등록
		// => 암호화 관련 bean을 등록할 경우 root-context.xml 또는 servlet-context.xml에 등록해도 상관없음
		//	  별도의 xml 파일로 해서 bean등록해보기, spring-security.xml
		// 3. 메서드 호출해서 암호화하기
		// 
		
		
		
		log.info("userPwd={}",m.getUserPwd());
		
		
		// 암호화 작업(암호문을 만들어내는 과정)
		// BCrytPasswordEncoder 객체로부터 encode 메서드 호출
		// [표현법]
		// bcryptPasswordEncoder.encode("평문") : String(암호문)
		String encPwd = bcryptPasswordEncoder.encode(m.getUserPwd());
		log.info("encPwd = {}",encPwd);
		
		// Member 객체의 userPwd 필드값을 암호문으로 바꾸기
		m.setUserPwd(encPwd);
				
		// Service로 요청 후 결과 받기
		int result = memberService.insertMember(m);		
		
		// 결과에 따른 응답 뷰 지정
		if(result>0) {
			session.setAttribute("alertMsg", "성공적으로 회원가입이 되었습니다.");
			mv.setViewName("redirect:/");
		}else {
			
			mv.addObject("errorMsg","로그인 실패");
			mv.setViewName("common/errorPage");			
		}
		
		return mv;
		
	}
	
	@RequestMapping("myPage.me")
	public String myPage() {
		
		return "member/myPage.jsp";
	}
	
	
	
	

	
	
	
}
