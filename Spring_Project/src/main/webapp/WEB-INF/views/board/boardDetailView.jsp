<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
table * {margin:5px;}
table {width:100%;}
        
 #piano {
  text-align : center;
  font-size : 30px;
 }
 
 label:hover {
  	color : pink;
 }
 
 key:hover{
 	background-color : lightgray;
 }
</style>
</head>
<body>
        
    <jsp:include page="../common/header.jsp" />

    <div class="content">
        <br><br>
        
        <div id="piano">
        <label class="black">■</label><label class="black">■</label>┃<label>■</label><label>■</label><label>■</label>┃<label>■</label><label>■</label><label>■</label>┃<label>■</label><label>■</label><label>■</label>┃<label>■</label><label>■</label><label>■</label>┃<label>■</label><label>■</label><label>■</label>┃<label>■</label><label>■</label><label>■</label> <br>
		<label id="key">┻</label>┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻┻ <br>
		♪~ ♬ ♪♬~♪ ♪~ ♬ ♪♬~♪ ♪~ ♬ ♪♬~♪ ♪~ ♬ ♪
        </div>
        <br><br>
        <div class="innerOuter">
            <h2>게시글 상세보기</h2>
            <br>

            <a class="btn btn-secondary" style="float:right;" href="list.bo">목록으로</a>
            <br><br>

            <table id="contentArea" align="center" class="table">
                <tr>
                    <th width="100">제목</th>
                    <td colspan="3">${requestScope.b.boardTitle}</td>
                </tr>
                <tr>
                    <th>작성자</th>
                    <td>${requestScope.b.boardWriter}</td>
                    <th>작성일</th>
                    <td>${requestScope.b.createDate }</td>
                </tr>
                <tr>
                    <th>첨부파일</th>
                    <td colspan="3">
                    	<c:choose>
                    		<c:when test="${empty requestScope.b.originName }">
                    			첨부파일이 없습니다.
                    		</c:when>
                    		<c:otherwise>
                    			<a href="${requestScope.b.changeName }" download="${requestScope.b.originName }">${requestScope.b.originName}</a>
                    		</c:otherwise>
                        	
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <th>내용</th>
                    <td colspan="3"></td>
                </tr>
                <tr>
                    <td colspan="4"><p style="height:150px;">${requestScope.b.boardContent }</p></td>
                </tr>
            </table>
            <br>

            <div align="center">
                <!-- 수정하기, 삭제하기 버튼은 이 글이 본인이 작성한 글일 경우에만 보여져야 함 -->
                <c:if test="${not empty sessionScope.loginUser and sessionScope.loginUser.userId eq requestScope.b.boardWriter}">
                	
                <a class="btn btn-primary" onclick="postFormSubmit(1);">수정하기</a>
                <a class="btn btn-danger" onclick="postFormSubmit(2);">삭제하기</a>
                
                <form id="postForm" action="" method="post">
                	<input type="hidden" name="bno" value="${requestScope.b.boardNo}">
                	<input type="hidden" name="filePath" value="${requestScope.b.changeName}">
                </form>
                
                <script>
                	function postFormSubmit(num){
                		
                		// num에 따라 위의 form 태그의 action 속성을 부여한 후 submit 시키기
                		
                		if(num==1){ // 수정하기
                			$("#postForm").attr("action","updateForm.bo").submit();
                		}else{ // 탈퇴하기
                			$("#postForm").attr("action","delete.bo").submit();
                		
                		// jQeury의 submit 메서드 : 해당 form의 submit 버튼을 누른 효과
                		}
                	}
                
                </script>
                </c:if>
            </div>
            <br><br>

            <!-- 댓글 기능은 나중에 ajax 배우고 나서 구현할 예정! 우선은 화면구현만 해놓음 -->
            <table id="replyArea" class="table" align="center">
                <thead>
                    <tr>
                    	<c:choose>
                    	<c:when test="${empty sessionScope.loginUser }">
	                    	<!-- 댓글창 막기 -->
	                    	<th colspan="2">
	                            <textarea class="form-control" cols="55" rows="2" style="resize:none; width:100%;" readonly>로구인 후 이용</textarea>
	                        </th>
	                        <th style="vertical-align:middle"><button class="btn btn-secondary" disabled>등록하기</button></th>
                    	</c:when>
                    	<c:otherwise>
                    	    <th colspan="2">
	                            <textarea class="form-control" name="" id="content" cols="55" rows="2" style="resize:none; width:100%;"></textarea>
	                        </th>
	                        <th style="vertical-align:middle"><button class="btn btn-secondary" onclick="addReply();">등록하기</button></th>
                    	</c:otherwise>
                    	</c:choose>
                    </tr>
                    <tr>
                        <td colspan="3">댓글(<span id="rcount">0</span>)</td>
                    </tr>
                </thead>
                <tbody>
                    
                </tbody>
            </table>
        </div>
        <br><br>

    </div>
    
    <script>
    	$(function(){
    		
    		// 댓글 리스트 조회용 선언적 함수 호출
    		selectReplyList();
    		
    		// 댓글 실시간
    		setInterval(selectReplyList,1000);
    	});
    	
    	
    	function addReply(){
    		
    		// 댓글 작성 요청용 ajax 요청
    		
    		// 댓글 내용이 있는지 먼저 검사 후
    		// 댓글 내용 중 공백 제거 후 길이가 0이 아닐경우 요청
    		// => textarea 가 form 태그 내부에 있지 않음
    		// required 속성으로 필수 입력값임을 나타낼 수 있음
    		if($("#content").val().trim().length != 0){
    			
    			$.ajax({
    				url : "rinsert.bo",
    				type : "get",
    				data : { // Ajax 요청 또한 Spring에서 커맨드 객체 방식 사용 가능
    					refBoardNo : ${requestScope.b.boardNo},
    					replyWriter : "${sessionScope.loginUser.userId}",
    					replyContent : $("#content").val()
    				},
    				success : function(result){
    					
    					if(result =="success"){    						   						
    						selectReplyList();
    						$("#content").val("");
    					}
    				
    					
    					
    				},
    				error : function(){
    					console.log("댓글 작성용 ajax 통신 실패");
    				
    				}
    				
    	    		
    			});
    			
    		}else{
    			alertify.alert("Alert","댓글 작성 후 등록 요청",function(){ alertify.success('ok');});
    		}
    		
    		
    	}
    	
    	function selectReplyList(){
    		
    		
    		// 해당 게시글에 딸린 댓들 조회 요청을 ajax 요청
    		$.ajax({
    			url  : "rlist.bo",
    			type : "get",
    			data : {bno : ${requestScope.b.boardNo}},
    			success : function(result){
    					let resultStr = ""
    					
    					for(let i=0;i<result.length;i++){
    						resultStr += "<tr>"
    								  		+"<th>"+result[i].replyWriter +"</th>"
    								  		+"<td>"+result[i].replyContent+"</td>"
    								  		+"<td>"+result[i].createDate+"</td>"
    								  	+"</tr>";
    					}			
    					$("#replyArea>tbody").html(resultStr);
    					$("#rcount").text(result.length);
    					
    			},
    			error : function(){
    				console.log("댓글 리스트 조회용 ajax 통신 실패");
    			}
    		});
    		
    	}
    	
    </script>
    
    
    <jsp:include page="../common/footer.jsp" />
    
</body>
</html>