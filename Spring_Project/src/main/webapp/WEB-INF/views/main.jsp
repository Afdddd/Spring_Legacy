<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인</title>
</head>
<body>

	<jsp:include page="common/header.jsp" />
	
	<div class="content">
		<br><br>
		
		<div class="innerOuter">
		
			<h4>게시글 Top5</h4>
			<br>
			<a href="list.bo" style="float:right;">더보기&raquo;</a>
			
			<table id="boardList" class="table table-hover" align="center" >
				<thead>
					<tr>
						<th>글번호</th>
						<th>제목</th>
						<th>작성자</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>첨부파일</th>
					</tr>					
				</thead>
				<tbody>
					<!-- 현재 조회수가 가장 높은 상위 5개의 게시글을 조회해서 ajax로 뿌리기 -->
				</tbody>
			</table>
		</div>
		<br><br>
		
	</div>
	
	<script>
		$(function(){
			
			topBoardList();
			
			setInterval(topBoardList,30000);
			
			$(document).on("click","#boardList>tbody>tr",function(){
				let bno = $(this).children().eq(0).text();
				
				location.href="detail.bo?bno="+bno;
			});
			
		});
		
		function topBoardList(){
			$.ajax({
				url :"topList.bo",
				type:"get",
				success:function(result){
						console.log(result);
						let resultStr = "";
    					
    					for(let i in result){
    						resultStr += "<tr>"
    								  		+"<th>"+result[i].boardNo +"</th>"
    								  		+"<td>"+result[i].boardTitle+"</td>"
    								  		+"<td>"+result[i].boardWriter+"</td>"
    								  		+"<td>"+result[i].count+"</td>"
    								  		+"<td>"+result[i].createDate+"</td>"
    								  		
    								  		if(result[i].originName != null){
    								  			resultStr += "<td>༼;´༎ຶ ۝ ༎ຶ༽</td>"	
    								  		}    								  		
    								  	+"</tr>";
    					}			   					
						$("#boardList>tbody").html(resultStr);    									
				},
				error:function(){
					console.log("실패")
				}
			});
		}
		
	
	</script>
	
	<jsp:include page="common/footer.jsp"/>

</body>
</html>