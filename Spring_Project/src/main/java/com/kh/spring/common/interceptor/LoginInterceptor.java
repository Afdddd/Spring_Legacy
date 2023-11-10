package com.kh.spring.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter  {

	// 1. preHandle(선처리)
	@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		//매개변수 -request  : 사용자의 요청 내용이 담겨있음
		//		 -response : 응답을 처리할 수 있는 객체
		//		 -handler  :  이 요청을 누가 처리하기로 했는가?
		
		
		//리턴값
		// - true  : 요청을 controller로 넘겨주겠다.
		// - false : 요청을 controller로 넘겨주지 않겠다.
	
//		return true;
		
		// 요청에 도달하기전에 로그인이 된 상태인지 검사하는 코드 추가
		HttpSession session = request.getSession();
		
		if(session.getAttribute("loginUser") != null) {
			// 로그인 된 상태
			return true;
		}else {
			// 로그인이 안된 상태
			
			session.setAttribute("alertMsg", "로그인 후 이용");
			
			// 메인페이지로 
			response.sendRedirect("/spring");
			return false;
		}
		
	}
	
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//		
//		// 매개변수
//		// request : 사용자의 요청
//		// response : 요청에 대한 응답정보를 담고있음
//		// handler  : 이 요청은 누가 처리하기로 했는가
//		// modelAndView : model(응답데이터) + view(응답페이지) 정보
//		
//		System.out.println("postHandle 잘넘어오나ㅏ");
//		System.out.println(handler);
//		System.out.println(modelAndView);
//		
//		// postHandle은 반환형이 없음
//		// => 이미 controller에서 요청 처리가 끝났기때문에
//	}
	
	
}
