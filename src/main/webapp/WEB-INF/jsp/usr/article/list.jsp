<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="ARTICLE ${board.code} LIST" />
<%@ include file="../common/head.jspf"%>

<!-- 회원간의 거리 구하기 -->
<c:if test="${param.boardId == 2}">
	<script>
		function calDistance(writerLatitude, writerLongitude, id) {
			
			const lat1 = writerLatitude; // 위도 1
    		const lon1 = writerLongitude; // 경도 1
    		const lat2 = ${rq.loginedMember.latitude}; // 위도 2
    		const lon2 = ${rq.loginedMember.longitude}; // 경도 2

			const distance = haversineDistance(lat1, lon1, lat2, lon2);
    		const result = Math.round(distance / 100) * 100;
			document.getElementById("result "+id).innerText = `약 `+ result +`m`;
		}

		function haversineDistance(lat1, lon1, lat2, lon2) {
			const R = 6371; // 지구 반지름 (km)
			const dLat = toRadians(lat2 - lat1);
			const dLon = toRadians(lon2 - lon1);
			const lat1Rad = toRadians(lat1);
			const lat2Rad = toRadians(lat2);

			const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
				Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1Rad) * Math.cos(lat2Rad);
			const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			const distance = R * c;

			return distance * 1000; // m 단위로 반환
		}

		function toRadians(degrees) {
			return degrees * Math.PI / 180;
		}
</script>
</c:if>

<!-- 검색, 리스트, 페이지네이션, 글쓰기  -->
<section class="mt-5 text-xl">
	<div class="container mx-auto px-3">
		<div class="flex justify-center mt-5 text-xl">
			<c:if
				test="${param.boardId == 1 || param.boardId == 3 || param.boardId == 2}">
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

		<table class="flex justify-center mt-5 text-xl"
			style="border-collapse: collapse; border-color: green">
			<tr>
				<c:if test="${boardId != 2}">
					<th>번호</th>
					<th>작성날짜</th>
				</c:if>
				<th>제목</th>
				<c:if test="${boardId == 2}">
					<th>매장</th>
					<th>회원 간의 거리</th>
					<th>배달비</th>
					<th>참여자</th>
					<th>마감</th>
				</c:if>
				<th>작성자</th>
				<c:if test="${boardId != 2}">
					<th>조회수</th>
				</c:if>
			</tr>

			<c:forEach var="article" items="${articles }">
				<c:if
					test="${boardId != 4 || rq.loginedMember.id == article.memberId || rq.loginedMember.id == 1}">
					<tr style="text-align: center;">
						<c:if test="${boardId != 2}">
							<td><div class="badge badge-lg text-xl">${article.id }</div></td>
							<td>${article.regDate.substring(0,10) }</td>
						</c:if>
						<td><a href="detail?id=${article.id }">${article.title }</a></td>
						<c:if test="${boardId == 2}">
							<td>${article.restaurantName }</td>
							<td><div id="result ${article.id}"></div></td>
							<td>${article.deliveryCost }원</td>
							<td>${article.participants }</td>
							<td>${article.deadlineTime.substring(5,16) }</td>
						</c:if>
						<td>${article.extra__writer }</td>
						<c:if test="${boardId != 2}">
							<td>${article.hitCount }</td>
						</c:if>
					</tr>
					<script>
					calDistance(${article.extra__writerLatitude},${article.extra__writerLongitude}, ${article.id})
					</script>
				</c:if>
			</c:forEach>
		</table>

		<div class="flex justify-center btn-group mt-5 text-xl">
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
		
		<div class="flex justify-center btn-group text-xl">
			<c:if test="${boardId == 1 && rq.loginedMember.authLevel == 7}">
				<a class="btn" href="/usr/article/write?boardId=1">소개 쓰기</a>
			</c:if>
			<c:if test="${boardId == 2}">
				<a class="btn" href="/usr/article/write?boardId=2">밥팅 쓰기</a>
			</c:if>
			<c:if test="${boardId == 3}">
				<a class="btn" href="/usr/article/write?boardId=3">글 쓰기</a>
			</c:if>
			<c:if test="${boardId == 4 && rq.loginedMember.authLevel != 7}">
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