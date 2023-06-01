<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="MEMBER MODIFY" />
<%@ include file="../common/head.jspf"%>
<hr />

<!-- 빈 칸 체크 -->
<script type="text/javascript">
	let MemberModify__submitFormDone = false;

	function MemberModify__submit(form) {
		if (MemberModify__submitFormDone) {
			return;
		}
		form.loginPw.value = form.loginPw.value.trim();

		if (form.loginPw.value.length > 0) {
			form.loginPwConfirm.value = form.loginPwConfirm.value.trim();

			if (form.loginPwConfirm.value.length == 0) {
				alert('비번 확인 써라');
				form.loginPwConfirm.focus();
				return;

			}

			if (form.loginPw.value != form.loginPwConfirm.value) {
				alert('비번 불일치');
				form.loginPw.focus();
				return;
			}
		}

		form.name.value = form.name.value.trim();
		form.nickname.value = form.nickname.value.trim();
		form.cellphoneNum.value = form.cellphoneNum.value.trim();
		form.email.value = form.email.value.trim();

		if (form.name.value.length == 0) {
			alert('이름 써라');
			form.name.focus();
		}

		if (form.nickname.value.length == 0) {
			alert('이름 써라');
			form.nickname.focus();
		}

		if (form.cellphoneNum.value.length == 0) {
			alert('cellphoneNum 써라');
			form.cellphoneNum.focus();
		}

		if (form.email.value.length == 0) {
			alert('email 써라');
			form.email.focus();
		}

		MemberModify__submitFormDone = true;
		form.submit();

	}

	function kakaoMapPost(longitude, latitude, name, address) {
		var action = './doCheckData';

		$.get(action, {
			isAjax : 'Y',
			longitude : longitude,
			latitude : latitude,
			name : name,
			address : address
		}, function(data) {
			$('.longitude').val(data.data1[0]);
			$('.latitude').val(data.data1[1]);
			$('.inputAddressName').val(data.data1[2]);
			$('.inputAddress').val(data.data1[3]);
			$('.name').text(data.data1[2]);
			$('.address').text(data.data1[3]);
		}, 'json');
	}
</script>

<!-- 회원 정보 수정 폼 -->
<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1">
			<form action="../member/doModify" method="POST"
				onsubmit="MemberModify__submit(this); return false;">
				<input value="${rq.loginedMember.id }" type="hidden" name="id" />
				<input value="${rq.loginedMember.loginId }" type="hidden" name="loginId" />
				<input value="${rq.loginedMember.name }" class ="inputAddressName" type="hidden" name="addressName" />
				<input value="${rq.loginedMember.address }" class = "inputAddress" type="hidden" name="address" />
				<input value="${rq.loginedMember.longitude }" class="longitude" type="hidden" name="longitude" />
				<input value="${rq.loginedMember.latitude }" class="latitude" type="hidden" name="latitude" />
				<table border="1">
					<colgroup>
						<col width="200" />
						<col width="400" />
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
							<th>새 비밀번호</th>
							<td class="text-left"><input name="loginPw"
								class="input input-bordered w-full max-w-xs"
								placeholder="새 비밀번호를 입력해주세요" type="text" /></td>
						</tr>
						<tr>
							<th>새 비밀번호 확인</th>
							<td class="text-left"><input name="loginPwConfirm"
								class="input input-bordered w-full max-w-xs"
								placeholder="새 비밀번호 확인을 입력해주세요" type="text" /></td>
						</tr>
						<tr>
							<th>이름</th>
							<td class="text-left"><input name="name" value="${rq.loginedMember.name }"
								class="input input-bordered w-full max-w-xs"
								placeholder="이름을 입력해주세요" type="text" /></td>
						</tr>
						<tr>
							<th>닉네임</th>
							<td class="text-left"><input name="nickname"
								value="${rq.loginedMember.nickname }"
								class="input input-bordered w-full max-w-xs"
								placeholder="닉네임을 입력해주세요" type="text" /></td>
						</tr>
						<tr>
							<th>전화번호</th>
							<td class="text-left"><input name="cellphoneNum"
								value="${rq.loginedMember.cellphoneNum }"
								class="input input-bordered w-full max-w-xs"
								placeholder="전화번호를 입력해주세요" type="text" /></td>
						</tr>
						<tr>
							<th>이메일</th>
							<td class="text-left"><input name="email" value="${rq.loginedMember.email }"
								class="input input-bordered w-full max-w-xs"
								placeholder="이메일을 입력해주세요" type="text" /></td>
						</tr>
						<tr>
							<th>주소</th>
							<td class="text-left"><div class="name">${rq.loginedMember.addressName }</div>
								<div class="address">${rq.loginedMember.address }</div>
								<div>
									<a class="btn btn-active btn-ghost text-xl" href="#"
										onclick="window.open('/usr/home/kakaoMap', '장소 수정','width=900, height=550'); return false">장소
										수정</a>
								</div></td>
						</tr>
						<tr>
							<th><button class="btn-text-link btn btn-active btn-ghost"
									type="button" onclick="history.back();">뒤로가기</button></th>
							<td>
								<button class="btn-text-link btn btn-active btn-ghost"
									type="submit" value="수정" /> 수정
								</button>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jspf"%>