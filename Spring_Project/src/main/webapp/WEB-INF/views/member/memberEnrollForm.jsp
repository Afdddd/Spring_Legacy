<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
</head>
<body>
    
    <!-- 메뉴바 -->
    <jsp:include page="../common/header.jsp" />

    <div class="content">
        <br><br>
        <div class="innerOuter">
            <h2>회원가입</h2>
            <br>

            <form action="insert.me" method="post" id="enrollForm">
                <div class="form-group">
                    <label for="userId">* ID : </label>
                    <input type="text" class="form-control" id="userId" placeholder="Please Enter ID" name="userId" required> 
                    
                    <!-- 아이디 사용 가능 여부를 출력할 div -->
                    <div id="checkResult" style="font-size:0.8em; display:none; "></div>
                    <br>
                    
                    <label for="userPwd">* Password : </label>
                    <input type="password" class="form-control" id="userPwd" placeholder="Please Enter Password" name="userPwd" required> <br>

                    <label for="checkPwd">* Password Check : </label>
                    <input type="password" class="form-control" id="checkPwd" placeholder="Please Enter Password" required> <br>

                    <label for="userName">* Name : </label>
                    <input type="text" class="form-control" id="userName" placeholder="Please Enter Name" name="userName" required> <br>

                    <label for="email"> &nbsp; Email : </label>
                    <input type="text" class="form-control" id="email" placeholder="Please Enter Email" name="email"> <br>

                    <label for="age"> &nbsp; Age : </label>
                    <input type="number" class="form-control" id="age" placeholder="Please Enter Age" name="age"> <br>

                    <label for="phone"> &nbsp; Phone : </label>
                    <input type="tel" class="form-control" id="phone" placeholder="Please Enter Phone (-없이)" name="phone"> <br>
                    
                    <label for="address"> &nbsp; Address : </label>
                    <input type="text" class="form-control" id="address" placeholder="Please Enter Address" name="address"> <br>
                    
                    <label for=""> &nbsp; Gender : </label> &nbsp;&nbsp;
                    <input type="radio" id="Male" value="M" name="gender" checked>
                    <label for="Male">남자</label> &nbsp;&nbsp;
                    <input type="radio" id="Female" value="F" name="gender">
                    <label for="Female">여자</label> &nbsp;&nbsp;
                </div> 
                <br>
                <div class="btns" align="center">
                    <button type="submit" class="btn btn-primary" disabled>회원가입</button>
                    <button type="reset" class="btn btn-danger">초기화</button>
                </div>
            </form>
        </div>
        <br><br>
    </div>
    
    <script>
    	$(function(){
    	
    		// 아이디를 입력받는 input 요소 객체를 변수에 담기
    		// (jQuery 방식으로 선택해서 담을 것)
    		let $idInput = $("#enrollForm input[name=userId]");
    		
    		$idInput.keyup(function(){

    			
    			// 우선 최소 5글자 이상으로 입려되어 있을 경우에만
    			// ajax를 요청해서 중복체크를 하도록
    			if($idInput.val().length >= 5){
    				
    				// 중복체크 요청 보내기
    				$.ajax({
    					url : "idCheck.me",
    					type : "get",
    					data : {checkId : $idInput.val()},
    					success : function(result){
    						if(result=="NNNNN"){ // 사용불가
    							$("#checkResult").show();
    							$("#checkResult").css("color","red").text("중복된 아이디가 존재합니다.");
    							
    							// 사용불가 아이디면 회원가입 버튼 비화성화
    							$("#enrollForm button[type=submit]").attr("disabled",true);
    						}else{ // 사용가능
    							$("#checkResult").show();
    							$("#checkResult").css("color","green").text("사용가능한 아이디 입니다.");
    							
    							// 회원가입 버튼 활성화
    							$("#enrollForm button[type=submit]").attr("disabled",false);
    						}
    						
    					},
    					error : function(){
    						console.log("아이디 중복 체크 실패")
    					}
    					
    					
    				});
    			}else{ // 5글자 미만
    				
    				// 회원 가입 버튼 비활성화    				
    				$("#enrollForm button[type=submit]").attr("disabled",true);
    				
    				$("#checkResult").show();
					$("#checkResult").css("color","red").text("5글자 이상 입력해주세요.");
    			}
    			
    			
    		})
    	})
    	
    </script>

    <!-- 푸터바 -->
    <jsp:include page="../common/footer.jsp" />

</body>
</html>