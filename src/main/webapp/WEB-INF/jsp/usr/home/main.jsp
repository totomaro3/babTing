<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="HOME MAIN" />
<%@ include file="../common/head.jspf"%>

<c:if test="${rq.isLogined()}">
	<script>
	//회원간의 거리 구하기
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

<div class="container mx-auto px-3">
	<a class="btn-text-link btn btn-active btn-ghost" href="/usr/chat/room">채팅방
		테스트</a>
</div>

<c:if test="${rq.isLogined()}">
	<section class="mt-8 text-3xl">
		<div class="container mx-auto px-3">
			<div>모든 밥팅</div>
			<table class="my-2"
				style="border-collapse: collapse; border-color: green">
				<tr>
					<th>제목</th>
					<th>매장</th>
					<th>회원 간의 거리</th>
					<th>배달비</th>
					<th>참여자</th>
					<th>마감</th>
					<td>작성자</td>
				</tr>
				<c:forEach var="article" items="${babtingArticles }">
					<tr style="text-align: center;">
						<td><a href="../article/detail?id=${article.id }">${article.title }</a></td>
						<td>${article.restaurantName }</td>
						<td><div id="result ${article.id}"></div></td>
						<td>${article.deliveryCost }</td>
						<td>${article.participants }</td>
						<td>${article.deadlineTime.substring(5,16) }</td>
						<td>${article.extra__writer }</td>
					</tr>
					<script>
					calDistance(${article.extra__writerLatitude},${article.extra__writerLongitude}, ${article.id})
					</script>
				</c:forEach>
			</table>
		</div>
	</section>

	<section class="mt-8 text-3xl">
		<div class="container mx-auto px-3">

			<div>공지사항</div>
			<table class="my-2"
				style="border-collapse: collapse; border-color: green">
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
		</div>
	</section>

	<section class="mt-8 text-3xl">
		<div class="container mx-auto px-3">
			<div>자유게시판</div>
			<table class="my-2"
				style="border-collapse: collapse; border-color: green">
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
		</div>
	</section>
</c:if>

<c:if test="${!rq.isLogined()}">
	<section class="mt-8 text-3xl">
		<div class="container mx-auto px-3">
			제작자 : 정호연 <br> 안녕하세요 배달 공동 구매 웹사이트 밥팅입니다.
		</div>
	</section>
</c:if>

<%@ include file="../common/foot.jspf"%>