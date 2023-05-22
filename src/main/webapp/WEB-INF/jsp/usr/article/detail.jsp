<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="게시물 상세 보기" />
<%@ include file="../common/head.jspf"%>

<!-- <iframe src="http://localhost:8081/usr/article/doIncreaseHitCountRd?id=2" frameborder="0"></iframe> -->
<script>
	const params = {}
	params.id = parseInt('${param.id}');
</script>

<!-- 조회수 증가 -->
<script>
	function ArticleDetail__increaseHitCount() {
		const localStorageKey = 'article__' + params.id + '__alreadyView';
		if (localStorage.getItem(localStorageKey)) {
			return;
		}
		localStorage.setItem(localStorageKey, true);
		$.get('../article/doIncreaseHitCountRd', {
			id : params.id,
			ajaxMode : 'Y'
		}, function(data) {
			$('.article-detail__hit-count').empty().html(data.data1);
		}, 'json');
	}

	$(function() {
		// 실전코드
		ArticleDetail__increaseHitCount();
		// 연습코드
		//setTimeout(ArticleDetail__increaseHitCount, 2000);
	})
</script>

<!-- 좋아요 관련 기능 -->
<script>

function ArticleDetail__increaseReaction() {
	$.get('../reaction/doIncreaseGoodReaction', {
		id : params.id,
		ajaxMode : 'Y'
	}, function(data) {
		$('.article-detail__reaction-point').empty().html(data.data1);
	}, 'json');
}

$(function() {
	$('#increment-button').click(function() {
		$.ajax({
			url : '/../reaction/doIncreaseGoodReactionRd', // 함수를 실행할 엔드포인트 URL
			type : 'POST',
			dataType : 'json',
			success : function(data) {
				$('.article-detail__reaction-point').text(data.data1); // 응답 데이터의 result 속성을 페이지에 적용
			},
			error : function() {
				alert('서버와 통신하는 중에 오류가 발생하였습니다.');
			}
		});
	});
});
</script>

<!-- 댓글은 3글자 이상 입력 가능 -->
<script type="text/javascript">
	let ReplyWrite__submitFormDone = false;
	function ReplyWrite__submitForm(form) {
		if (ReplyWrite__submitFormDone) {
			return;
		}
		form.body.value = form.body.value.trim();
		if (form.body.value.length < 3) {
			alert('3글자 이상 입력하세요');
			form.body.focus();
			return;
		}
		ReplyWrite__submitFormDone = true;
		form.submit();
	}
</script>

<!-- 회원간의 거리 구하기 -->
<c:if test="${article.boardId == 2}">
	<script>
		window.onload = function() {
			
			const lat1 = ${article.extra__writerLatitude}; // 위도 1
    		const lon1 = ${article.extra__writerLongitude}; // 경도 1
    		const lat2 = ${rq.loginedMember.latitude}; // 위도 2
    		const lon2 = ${rq.loginedMember.longitude}; // 경도 2

			const distance = haversineDistance(lat1, lon1, lat2, lon2);
    		const result = Math.round(distance / 100) * 100;
			document.getElementById("result").innerText = `약 `+ result +`m`;
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

<section class="mt-3 text-xl">
	<div class="container mx-auto px-3">
		<!-- 위의 버튼 -->
		<div class="button">
			<button class="btn btn-active btn-ghost text-xl" type="button"
				onclick="history.back();">뒤로가기</button>
			&nbsp&nbsp
			<c:if test="${article.memberId eq loginedMemberId}">
				<a class="btn btn-active btn-ghost text-xl"
					href="../article/modify?id=${article.id }">수정</a>
			</c:if>
			&nbsp&nbsp
			<c:if test="${article.memberId eq loginedMemberId}">
				<a class="btn btn-active btn-ghost text-xl"
					onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;"
					href="../article/doDelete?id=${article.id }">삭제</a>
			</c:if>
			&nbsp&nbsp
			<c:if
				test="${article.memberId eq loginedMemberId && article.boardId == 2 && article.deadStatus == 0}">
				<a class="btn btn-active btn-ghost text-xl"
					href="../article/doDeadLine?id=${article.id }">마감</a>
			</c:if>
			&nbsp&nbsp
			<c:if
				test="${article.memberId eq loginedMemberId && article.boardId == 2 && article.deadStatus == 1}">
				<a class="btn btn-active btn-ghost text-xl"
					href="../article/doCancelDeadArticle?id=${article.id }">마감 취소</a>
			</c:if>
		</div>

		<!-- 게시글 상세 보기 -->
		<table class="mt-3">
			<colgroup>
				<col width="200" />
				<col width="600" />
			</colgroup>
			<tr>
				<th>작성날짜</th>
				<td class="text-left">${article.regDate }</td>
			</tr>
			<tr>
				<th>수정날짜</th>
				<td class="text-left">${article.updateDate }</td>
			</tr>
			<tr>
				<th>작성자</th>
				<td class="text-left">${article.extra__writer }</td>
			</tr>
			<c:if test="${article.boardId == 2}">
				<tr>
					<th>예상 배달 비용</th>
					<td class="text-left">${article.deliveryCost }원</td>
				</tr>
				<tr>
					<th>회원 간의 거리</th>
					<td class="text-left"><div id="result"></div></td>
				</tr>
				<tr>
					<th>매장</th>
					<td>
						<div class="text-left">${article.restaurantName }</div>
						<div class="text-left">${article.address }</div>
						<div id="map" style="width: 100%; height: 350px;"></div> <script
							type="text/javascript"
							src="//dapi.kakao.com/v2/maps/sdk.js?appkey=9ae1891ba1b0c1ff630450e76b284f50"></script>
						<script>
							var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
							mapOption = {
								center : new kakao.maps.LatLng(${article.latitude },
										${article.longitude }), // 지도의 중심좌표
								level : 3
							// 지도의 확대 레벨
							};

							var map = new kakao.maps.Map(mapContainer,
									mapOption); // 지도를 생성합니다

							// 마커가 표시될 위치입니다 
							var markerPosition = new kakao.maps.LatLng(
									${article.latitude }, ${article.longitude });

							// 마커를 생성합니다
							var marker = new kakao.maps.Marker({
								position : markerPosition
							});

							// 마커가 지도 위에 표시되도록 설정합니다
							marker.setMap(map);

							// 아래 코드는 지도 위의 마커를 제거하는 코드입니다
							// marker.setMap(null);
						</script>
					</td>
				</tr>
				<tr>
					<th>모집 마감 시간</th>
					<td class="text-left">${article.deadlineTime.substring(5,16)}</td>
				</tr>
			</c:if>
			<c:if test="${article.boardId == 3}">
				<tr>
					<th>조회수</th>
					<td class="text-left"><span class="article-detail__hit-count">${article.hitCount }</span>
					</td>
				</tr>
				<tr>
					<th>추천</th>
					<td class="text-left"><span>&nbsp;좋아요 : ${article.goodReactionPoint }&nbsp;</span>
						<span>&nbsp;싫어요 : ${article.badReactionPoint }&nbsp;</span> <c:if
							test="${article.boardId == 3}">
							<div>
								<span> <span>&nbsp;</span> <a
									href="/usr/reactionPoint/doGoodReaction?relTypeCode=article&relId=${param.id }&replaceUri=${rq.encodedCurrentUri}"
									class="btn btn-xs ${actorHasGoodReaction ? "deepSkyBlue" : ""}">좋아요
										👍</a>
								</span> <span> <span>&nbsp;</span> <a
									href="/usr/reactionPoint/doBadReaction?relTypeCode=article&relId=${param.id }&replaceUri=${rq.encodedCurrentUri}"
									class="btn btn-xs ${actorHasBadReaction ? "deepSkyBlue" : ""}">싫어요
										👎</a>
								</span>
							</div>
						</c:if></td>
				</tr>
			</c:if>
			<tr>
				<th>제목</th>
				<td class="text-left">${article.title }</td>
			</tr>
			<tr>
				<th>내용</th>
				<td class="text-left">${article.body }</td>
			</tr>
		</table>
		<br>
		
		<c:if
			test="${article.boardId == 2 || rq.loginedMember.authLevel == 7}">
			<div>해당 배달 음식 공동 구매에 참여하시려면 대화방에 참여해주세요!</div>
			<a class="btn-text-link btn btn-active btn-ghost text-xl"
				onclick="window.open('/usr/chat/moveChating?roomName=${article.title}&roomNumber=${article.id}', '대화방 참여 하기','width=750, height=850'); return false">대화방
				참여 하기</a>
		</c:if>

		<!-- 댓글 작성 -->
		<c:if
			test="${article.boardId == 3 || rq.loginedMember.authLevel == 7}">
			<section class="mt-3 text-xl">
				<div class="container mx-auto px-3">
					<div class="table-box-type-1">
						<c:if test="${rq.isLogined() }">
							<form action="../reply/doWrite" method="POST"
								onsubmit="ReplyWrite__submitForm(this); return false;">
								<input type="hidden" name="relTypeCode" value="article" /> <input
									type="hidden" name="relId" value="${article.id }" />
								<table>
									<colgroup>
										<col width="200" />
									</colgroup>

									<tbody>
										<tr>
											<th>댓글</th>
											<td><textarea
													class="input input-bordered w-full max-w-xs" type="text"
													name="body" placeholder="내용을 입력해주세요" /></textarea></td>
											<td>
												<button type="submit" value="작성" /> 댓글 작성
												</button>
											</td>
										</tr>
									</tbody>
								</table>
							</form>
						</c:if>
						<c:if test="${!rq.isLogined() && article.boardId == 3}">
				댓글을 작성하려면 <a class="btn-text-link btn btn-active btn-ghost"
								href="/usr/member/login?afterLoginUri=${rq.getEncodedCurrentUri()}">로그인</a> 후 이용해주세요.
				</c:if>
					</div>
				</div>
			</section>
		</c:if>

		<!-- 댓글 리스트 -->
		<c:if test="${article.boardId == 4 || article.boardId == 3 }">
			<section class="mt-3 text-xl">
				<div class="container mx-auto px-3">
					<h1 class="text-3xl">댓글 리스트(${repliesCount })</h1>
					<table class="table table-zebra w-full">
						<colgroup>
							<col width="70" />
							<col width="100" />
							<col width="100" />
							<col width="50" />
							<col width="140" />
							<col width="140" />
						</colgroup>
						<thead>
							<tr>
								<th>번호</th>
								<th>날짜</th>
								<th>작성자</th>
								<th>내용</th>
								<th>수정</th>
								<th>삭제</th>
							</tr>
						</thead>

						<tbody>
							<c:forEach var="reply" items="${replies }">
								<tr class="hover">
									<td>
										<div class="badge">${reply.id}</div>
									</td>
									<td>${reply.regDate.substring(2,16)}</td>
									<td>${reply.extra__writer}</td>
									<td align="left">${reply.body}</td>
									<td><c:if test="${reply.memberId eq loginedMemberId}">
											<a class="btn btn-active btn-ghost text-xl"
												href="../reply/modify?id=${reply.id }">수정</a>
										</c:if></td>
									<td><c:if test="${reply.memberId eq loginedMemberId}">
											<a class="btn btn-active btn-ghost text-xl"
												onclick="if(confirm('정말 삭제하시겠습니까?')==false) return false;"
												href="../reply/doDelete?id=${reply.id }">삭제</a>
										</c:if></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</section>
		</c:if>
	</div>
</section>






<%@ include file="../common/foot.jspf"%>