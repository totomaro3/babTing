<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="MEMBER MYPAGE" />
<%@ include file="../common/head.jspf"%>
<%@ page import="com.babTing.toto.demo.util.Ut"%>

<!-- 내 정보 보기 폼 -->
<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1">
			<table border="1">
				<colgroup>
					<col width="200" />
					<col width="600" />
				</colgroup>

				<tbody>
					<tr>
						<th>가입일</th>
						<td class="text-left">${rq.loginedMember.regDate }</td>
					</tr>
					<tr>
						<th>아이디</th>
						<td class="text-left">${rq.loginedMember.loginId }</td>
					</tr>
					<tr>
						<th>이름</th>
						<td class="text-left">${rq.loginedMember.name }</td>
					</tr>
					<tr>
						<th>닉네임</th>
						<td class="text-left">${rq.loginedMember.nickname }</td>
					</tr>
					<tr>
						<th>전화번호</th>
						<td class="text-left">${rq.loginedMember.cellphoneNum }</td>
					</tr>
					<tr>
						<th>이메일</th>
						<td class="text-left">${rq.loginedMember.email }</td>
					</tr>
					<tr>
						<th>주소</th>
						<td><div class="text-left">${rq.loginedMember.addressName }</div>
						<div class="text-left">${rq.loginedMember.address }</div>
						<div id="map" style="width: 100%; height: 350px;"></div>
						 <script
							type="text/javascript"
							src="//dapi.kakao.com/v2/maps/sdk.js?appkey=9ae1891ba1b0c1ff630450e76b284f50"></script>
						<script>
							var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
							mapOption = {
								center : new kakao.maps.LatLng(${rq.loginedMember.latitude },
										${rq.loginedMember.longitude }), // 지도의 중심좌표
								level : 3
							// 지도의 확대 레벨
							};

							var map = new kakao.maps.Map(mapContainer,
									mapOption); // 지도를 생성합니다

							// 마커가 표시될 위치입니다 
							var markerPosition = new kakao.maps.LatLng(
									${rq.loginedMember.latitude }, ${rq.loginedMember.longitude });

							// 마커를 생성합니다
							var marker = new kakao.maps.Marker({
								position : markerPosition
							});

							// 마커가 지도 위에 표시되도록 설정합니다
							marker.setMap(map);

							// 아래 코드는 지도 위의 마커를 제거하는 코드입니다
							// marker.setMap(null);
						</script></td>
					</tr>
					<tr>
						<th><button class="btn-text-link btn btn-active btn-ghost"
								type="button" onclick="history.back();">뒤로가기</button></th>
						<td><a href="../member/checkPw"
							class="btn btn-active btn-ghost">회원정보 수정</a></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jspf"%>