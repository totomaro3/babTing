<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="HOME MAIN" />
<%@ include file="../common/head.jspf" %>

<div>모든 밥팅</div>
<table class="my-2" style="border-collapse: collapse; border-color: green">
		<tr>
			<th>제목</th>
			<th>매장</th>
			<th>거리</th>
			<th>배달비</th>
			<th>참여자</th>
			<th>마감</th>
		</tr>
	<c:forEach var="article" items="${babtingArticles }">
		<tr style="text-align: center;">
			<td><a href="../article/detail?id=${article.id }">${article.title }</a></td>
			<td>${article.restaurantName }</td>
			<td>약 ${article.distance }m</td>
			<td>${article.deliveryCost }</td>
			<td>${article.participants }</td>
			<td>${article.deadlineTime.substring(11,16) }</td>
		</tr>
	</c:forEach>
</table>

<div>공지사항</div>
<table class="my-2" style="border-collapse: collapse; border-color: green">
		<tr>
			<th>번호</th>
			<th>작성날짜</th>
			<th>제목</th>
			<th>작성자</th>
			<th>조회수</th>
		</tr>
	<c:forEach var="article" items="${noticeArticles }">
		<tr style="text-align: center;">
			<td><div class="badge badge-lg text-xl">${article.id }</div></td>
			<td>${article.regDate.substring(0,10) }</td>
			<td><a href="../article/detail?id=${article.id }">${article.title }</a></td>
			<td>${article.extra__writer }</td>
			<td>${article.hitCount }</td>
		</tr>
	</c:forEach>
</table>

<div>자유게시판</div>
<table class="my-2" style="border-collapse: collapse; border-color: green">
		<tr>
			<th>번호</th>
			<th>작성날짜</th>
			<th>제목</th>
			<th>작성자</th>
			<th>조회수</th>
		</tr>
	<c:forEach var="article" items="${freeArticles }">
		<tr style="text-align: center;">
			<td><div class="badge badge-lg text-xl">${article.id }</div></td>
			<td>${article.regDate.substring(0,10) }</td>
			<td><a href="../article/detail?id=${article.id }">${article.title }</a></td>
			<td>${article.extra__writer }</td>
			<td>${article.hitCount }</td>
		</tr>
	</c:forEach>
</table>
	
<%@ include file="../common/foot.jspf"%>