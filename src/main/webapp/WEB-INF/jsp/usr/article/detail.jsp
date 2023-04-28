<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="ARTICLE DETAIL" />
<%@ include file="../common/head.jspf"%>
<table>
	<tr>
		<th>번호</th>
		<td>
		<div class="badge badge-lg text-xl">${article.id }</div>
		</td>
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
		<th>조회수</th>
		<td>${article.hitCount }</td>
	</tr>
	<tr>
		<th>제목</th>
		<td>${article.title }</td>
	</tr>
	<tr>
		<th>내용</th>
		<td>${article.body }</td>
	</tr>
</table>
<div class="button">
	<button class="btn btn-active btn-ghost text-xl" type="button" onclick="history.back();">뒤로가기</button>
	<c:if test="${article.memberId eq loginedMemberId}">
		<a class="btn btn-active btn-ghost text-xl" href="../article/modify?id=${article.id }">수정</a>
	</c:if>
	<c:if test="${article.memberId eq loginedMemberId}">
		<a class="btn btn-active btn-ghost text-xl" onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;"
			href="../article/doDelete?id=${article.id }">삭제</a>
	</c:if>
</div>


<%@ include file="../common/foot.jspf"%>