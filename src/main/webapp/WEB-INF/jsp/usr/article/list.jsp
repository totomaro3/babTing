<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="ARTICLE ${board.code} LIST" />
<%@ include file="../common/head.jspf"%>

<section class="mt-5 text-xl">
	<div class="container mx-auto px-3">
		<div class="flex">
			<div>
				게시물 갯수 : <span class="badge">${articlesCount }</span> 개
			</div>
			<div class="w-1/12"></div>
			<c:if test="${param.boardId == 1 || param.boardId == 3 }">
				<div class="flex">
					<form class="flex" method="post"
						action="?boardId=${boardId }&page=1">
						<select name="searchKeywordTypeCode"
							class="select select-bordered max-w-xs">
							<option value="title" selected>제목</option>
							<option value="body"
								${searchKeywordTypeCode == "body" ? "selected" : ""}>내용</option>
							<option value="title,body"
								${searchKeywordTypeCode == "title,body" ? "selected" : ""}>제목+내용</option>
						</select> <input class="input input-bordered w-full max-w-xs"
							value="${searchKeyword }" type="text" name="searchKeyword"
							placeholder="검색어를 입력해주세요" />
						<button class="button btn btn-active btn-ghost text-xl"
							type="submit">검색</button>
					</form>
				</div>
			</c:if>
		</div>

		<table class="mt-5 text-xl"
			style="border-collapse: collapse; border-color: green">
			<tr>
				<c:if test="${boardId != 2}">
					<th>번호</th>
					<th>작성날짜</th>
				</c:if>
				<th>제목</th>
				<c:if test="${boardId == 2}">
					<th>매장</th>
					<th>거리</th>
					<th>배달비</th>
					<th>참여자</th>
					<th>마감</th>
				</c:if>
				<c:if test="${boardId != 2}">
					<th>작성자</th>
					<th>조회수</th>
				</c:if>
			</tr>
			<c:forEach var="article" items="${articles }">
				<tr style="text-align: center;">
					<c:if test="${boardId != 2}">
						<td><div class="badge badge-lg text-xl">${article.id }</div></td>
						<td>${article.regDate.substring(0,10) }</td>
					</c:if>
					<td><a href="detail?id=${article.id }">${article.title }</a></td>
					<c:if test="${boardId == 2}">
						<td>${article.restaurantName }</td>
						<td>약 ${article.distance }m</td>
						<td>${article.deliveryCost }</td>
						<td>${article.participants }</td>
						<td>${article.deadlineTime.substring(11,16) }</td>
					</c:if>
					<c:if test="${boardId != 2}">
						<td>${article.extra__writer }</td>
						<td>${article.hitCount }</td>
					</c:if>
				</tr>
			</c:forEach>
		</table>

		<div class="btn-group mt-5 text-xl">
			<c:set var="paginationLen" value="3" />
			<c:set var="startPage"
				value="${page - paginationLen >= 1 ? page - paginationLen : 1}" />
			<c:set var="endPage"
				value="${page + paginationLen <= pagesCount ? page + paginationLen : pagesCount}" />

			<c:set var="baseUri" value="?boardId=${boardId }" />
			<c:set var="baseUri"
				value="${baseUri }&searchKeywordTypeCode=${searchKeywordTypeCode}" />
			<c:set var="baseUri"
				value="${baseUri }&searchKeyword=${searchKeyword}" />

			<c:if test="${startPage > 1 }">
				<a class="btn" href="${baseUri }&page=1">1</a>
				<button class="btn btn-disabled">...</button>
			</c:if>

			<c:forEach begin="${startPage }" end="${endPage }" var="i">
				<a class="btn ${page == i ? 'btn-active' : '' }"
					href="${baseUri }&page=${i }">${i }</a>
			</c:forEach>

			<c:if test="${endPage < pagesCount }">
				<button class="btn btn-disabled">...</button>
				<a class="btn" href="${baseUri }&page=${pagesCount }">${pagesCount }</a>
			</c:if>
		</div>

		<br>
		<div class="btn-group mt-5 text-xl">
			<c:if test="${boardId == 1 && loginedMemberAuthLevel == 7}">
				<a class="btn" href="/usr/article/write?boardId=1">소개 쓰기</a>
			</c:if>
			<c:if test="${boardId == 2}">
				<a class="btn" href="/usr/article/write?boardId=2">밥팅 쓰기</a>
			</c:if>
			<c:if test="${boardId == 3}">
				<a class="btn" href="/usr/article/write?boardId=3">글 쓰기</a>
			</c:if>
			<c:if test="${boardId == 4 && loginedMemberAuthLevel != 7}">
				<a class="btn" href="/usr/article/write?boardId=4">문의 하기</a>
			</c:if>
		</div>
	</div>
</section>

<style type="text/css">
.page>a {
	color: black;
}

.page>a.red {
	color: red;
}
</style>
<%@ include file="../common/foot.jspf"%>