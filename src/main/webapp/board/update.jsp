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
		<h3>삭제하기</h3>
		<form action="delelte_ok.do" method="post">
			<table id="table_content" width="600">
				<tr>
					<th width="20%">이름</th>
					<td width="80%">
						<input type="text" name="name" size="12" value="${vo.name }"/>
						<input type="hidden" name="no" value="${vo.no }"/>
					</td>
				</tr>		
				<tr>
					<th width="20%">제목</th>
					<td width="80%">
						<input type="text" name="subject" size="20" value="${vo.subject }"/>
					</td>
				</tr>		
				<tr>
					<th width="20%">내용</th>
					<td width="80%">
						<textarea name="content" cols="55" rows="10" >${vo.content }</textarea>
					</td>
				</tr>		
				<tr>
					<th width="20%">비밀번호</th>
					<td width="80%">
						<input type="password" name="pwd" size="10" />
						<input type="hidden" name="no" value="${vo.no }" />
					</td>
				</tr>		
				<tr>
					<td colspan="2" align="center">
						<input type="submit" value="삭제" />
						<input type="button" value="취소" onclick="javascript:history.back();" />
					</td>
				</tr>
			</table>
		</form>
	</center>
</body>
</html>