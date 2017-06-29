<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE>
<html >
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="table.css" />
<script>

</script>
</head>
<body>
	<center>
		<h3>내용보기</h3>
		<table id="table_content" width="600">
			<tr>
				<th width="20%">번호</th>
				<td width="30%" align="center">${vo.no }</td>
				<th width="20%">작성일</th>
				<td width="30%" align="center">${vo.regdate }</td>
			</tr>
			<tr>
				<th width="20%">이름</th>
				<td width="30%" align="center">${vo.name }</td>
				<th width="20%">조회수</th>
				<td width="30%" align="center">${vo.hit }</td>
			</tr>
			<tr>
				<th width="20%">제목</th>
				<td colspan="3" align="left">${vo.subject }</td>
			</tr>
			<tr>
				<%-- <td colspan="4" align="left" valign="top" height="100"><pre>${vo.content }</pre></td> --%><!-- pre쓸때는 td사이에 공백을 주면 안된다. -->
				<td colspan="4" align="left" valign="top" height="100">${vo.content }</td>
			</tr>
		</table>
		<table id="table_content" width="600">
			<tr>
				<td align="right">
					<a href="update.do?no=${vo.no }">수정</a>&nbsp;
					<a href="delete.do?no=${vo.no }">삭제</a>&nbsp;
					<a href="list.do">목록</a><!-- history.back하면 조회수가 안맞는다. -->
				</td>
			</tr>
		</table>
		<table id="table_content" width="600">
			<tr>
				<td align="center">
					<img src="result.png" alt="" />
				</td>
			</tr>
		</table>
	</center>
</body>
</html>