package com.kh.spring.member.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * Lombok(롬복)
 * - 자동 코드 생성 라이브러리
 * - Vo 작성시 반복되는 getter/setter, toString, 생성자 등
 * 메서드 작성 코드를 줄여주는 "코드 다이어트" 라이브러리
 * 
 * Lombok 설치 방법
 * 1. Maven pom.xml으로 라이브러리 다운 후 적용
 * 2. 다운로드된 jar 파일을 찾아서 실행
 * 3. IDE (이클립스, STS) 를 재실행
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {
	
	private String userId; 	//	USER_ID	VARCHAR2(30 BYTE)
	private String userPwd;	//	USER_PWD	VARCHAR2(100 BYTE)
	private String userName;//	USER_NAME	VARCHAR2(15 BYTE)
	private String email;	//	EMAIL	VARCHAR2(100 BYTE)
	private String gender;	//	GENDER	VARCHAR2(1 BYTE)
	private String age;		//	AGE	NUMBER
	private String phone;	//	PHONE	VARCHAR2(13 BYTE)
	private String address; //	ADDRESS	VARCHAR2(100 BYTE)
	private Date enrollDate;//	ENROLL_DATE	DATE
	private Date modifyDate;//	MODIFY_DATE	DATE
	private String status;	//	STATUS	VARCHAR2(1 BYTE)
	
}
