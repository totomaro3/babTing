<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="메인 웹페이지" />
<%@ include file="../common/head.jspf"%>

<!-- 회원간의 거리 구하기 -->
<c:if test="${rq.isLogined()}">
	<script>
		function calDistance(writerLatitude, writerLongitude, id) {
			
			const lat1 = writerLatitude; // 위도 1
    		const lon1 = writerLongitude; // 경도 1
    		const lat2 = ${rq.loginedMember.latitude}; // 위도 2
    		const lon2 = ${rq.loginedMember.longitude}; // 경도 2

			const distance = haversineDistance(lat1, lon1, lat2, lon2);
    		const result = Math.round(distance / 100) * 100;
			document.getElementById("result1 "+id).innerText = `약 `+ result +`m`;
			document.getElementById("result2 "+id).innerText = `약 `+ result +`m`;
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

<!-- 로그인 시 메인페이지에 글 보기 -->
<c:if test="${rq.isLogined()}">
	<section class="mt-8">
		<div class="container mx-auto px-3">
			<div>맞춤 밥팅 <button></button></div>
			<table class="my-2"
				style="border-collapse: collapse; border-color: green">
				<tr>
					<th>매장</th>
					<th>회원 간의 거리</th>
					<th>배달비</th>
					<th>제목</th>
					<th>참여자</th>
					<th>마감시간</th>
				</tr>
				<c:forEach var="article" items="${babtingArticles }">
					<tr style="text-align: center;">
						<td>${article.restaurantName }</td>
						<td><div id="result2 ${article.id}"></div></td>
						<td>${article.deliveryCost }</td>
						<td><a href="../article/detail?id=${article.id }">${article.title }</a></td>
						<td>${article.participants }</td>
						<td>${article.deadlineTime.substring(11,16) }</td>
					</tr>
					<script>
					calDistance(${article.extra__writerLatitude},${article.extra__writerLongitude}, ${article.id})
					</script>
				</c:forEach>
			</table>

			<div>모든 밥팅</div>
			<table class="my-2"
				style="border-collapse: collapse; border-color: green">
				<tr>
					<th>매장</th>
					<th>회원 간의 거리</th>
					<th>배달비</th>
					<th>제목</th>
					<th>참여자</th>
					<th>마감시간</th>
				</tr>
				<c:forEach var="article" items="${babtingArticles }">
					<tr style="text-align: center;">
						<td>${article.restaurantName }</td>
						<td><div id="result1 ${article.id}"></div></td>
						<td>${article.deliveryCost }</td>
						<td><a href="../article/detail?id=${article.id }">${article.title }</a></td>
						<td>${article.participants }</td>
						<td>${article.deadlineTime.substring(11,16) }</td>
					</tr>
					<script>
					calDistance(${article.extra__writerLatitude},${article.extra__writerLongitude}, ${article.id})
					</script>
				</c:forEach>
			</table>


		</div>
	</section>

	<section class="mt-8">
		<div class="container mx-auto px-3">
			<div>공지사항</div>
			<table class="my-2"
				style="border-collapse: collapse; border-color: green">
				<tr>
					<th>작성날짜</th>
					<th>제목</th>
				</tr>
				<c:forEach var="article" items="${noticeArticles }">
					<tr style="text-align: center;">
						<td>${article.regDate.substring(5,10) }</td>
						<td><a href="../article/detail?id=${article.id }">${article.title }</a></td>
					</tr>
				</c:forEach>
			</table>

			<div>자유게시판</div>
			<table class="my-2"
				style="border-collapse: collapse; border-color: green">
				<tr>
					<th>작성날짜</th>
					<th>제목</th>
				</tr>
				<c:forEach var="article" items="${freeArticles }">
					<tr style="text-align: center;">
						<td>${article.regDate.substring(5,10) }</td>
						<td><a href="../article/detail?id=${article.id }">${article.title }</a></td>
					</tr>
				</c:forEach>
			</table>

		</div>
	</section>
</c:if>

<!-- 로그아웃 시 메인페이지 설정 -->
<c:if test="${!rq.isLogined()}">
	<section class="mt-8 text-3xl">
		<div class="container mx-auto px-3">
			제작자 : 정호연
			<br> 해마다 늘어나서 점점 부담스러워지는 배달 비용!
			<br> 이제 혼자서는 감당하지 마세요~ 
			<br> 편리하고 간편하게 같이 시켜먹을 사람을 찾아 주는
			<br> 배달 음식 공동 구매 모집 웹 어플리케이션 사이트
			<br> 밥팅을 지금 시작해보세요!
		</div>
	</section>
</c:if>

<%@ include file="../common/foot.jspf"%>