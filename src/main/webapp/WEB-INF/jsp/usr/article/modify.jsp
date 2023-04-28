<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="ARTICLE MODIFY" />
<%@ include file="../common/head.jspf"%>

<form method="post" action="doModify">
	<input value="${article.id }" type="hidden" name="id" />
	<table>
		<tr>
			<th>번호</th>
			<td>${article.id }</td>
		</tr>
		<tr>
			<th>작성날짜</th>
			<td>${article.regDate }</td>
		</tr>
		<tr>
			<th>수정날짜</th>
			<td>${article.updateDate }</td>
		</tr>
		<tr>
			<th>작성자</th>
			<td>${article.extra__writer }</td>
		</tr>
		<tr>
			<th>제목</th>
			<td><input class="input input-bordered w-full max-w-xs" value="${article.title }" type="text" name="title"
				placeholder="제목을 입력해주세요" /></td>
		</tr>
		<tr>
			<th>내용</th>
			<td><textarea class="input input-bordered w-full max-w-xs" name="body" placeholder="내용을 입력해주세요" />${article.body }</textarea></td>
		</tr>
	</table>
	<button type="submit">수정</button>
</form>
<button type="button" onclick="history.back();">뒤로가기</button>

<%@ include file="../common/foot.jspf"%>
