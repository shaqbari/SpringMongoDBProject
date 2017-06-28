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
		<h3>게시판</h3>
		<table id="table_content" width="700">
			<tr>
				<td align="left">
					<a href="insert.do">새글</a>
				</td><!-- ie는 기본center정렬이므로 정렬을 써주는 것이 좋다. -->
			</tr>
		</table>
		<table id="table_content" width="700">
			<tr>
				<th width="10%">번호</th>
				<th width="45%">제목</th>
				<th width="15%">이름</th>
				<th width="20%">작성일</th>
				<th width="10%">조회수</th>
			</tr>
			<c:forEach var="vo" items="${list}">
				<tr>
					<td width="10%" align="center">${vo.no }</td>
					<td width="45%" align="left">${vo.subject }</td>
					<td width="15%" align="center">${vo.name }</td>
					<td width="20%" align="center">${vo.regdate }</td>
					<td width="10%" align="center">${vo.hit }</td>
				</tr>
			</c:forEach>
		</table>
	</center>
</body>
</html>