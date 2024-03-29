<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="MEMBER JOIN" />
<%@ include file="../common/head.jspf"%>

<!-- 빈 칸 체크 -->
<script>
	let submitJoinFormDone = false;
	function submitJoinForm(form) {
		/*
		if (submitJoinFormDone) {
			alert('처리중입니다');
			return;
		}
		*/
		form.loginId.value = form.loginId.value.trim();
		if (form.loginId.value == 0) {
			alert('아이디를 입력해주세요');
			return;
		}
		if (form.loginId.value != validLoginId) {
			alert('사용할 수 없는 아이디입니다');
			form.loginId.focus();
			return;
		}
		form.loginPw.value = form.loginPw.value.trim();
		if (form.loginPw.value == 0) {
			alert('비밀번호를 입력해주세요');
			return;
		}
		form.loginPwConfirm.value = form.loginPwConfirm.value.trim();
		if (form.loginPwConfirm.value == 0) {
			alert('비밀번호 확인을 입력해주세요');
			return;
		}
		if (form.loginPwConfirm.value != form.loginPw.value) {
			alert('비밀번호가 일치하지 않습니다');
			form.loginPw.focus();
			return;
		}
		form.name.value = form.name.value.trim();
		if (form.name.value == 0) {
			alert('이름을 입력해주세요');
			return;
		}
		form.nickname.value = form.nickname.value.trim();
		if (form.nickname.value == 0) {
			alert('닉네임을 입력해주세요');
			return;
		}
		form.email.value = form.email.value.trim();
		if (form.email.value == 0) {
			alert('이메일을 입력해주세요');
			return;
		}
		if (form.email.value != validEmail) {
			alert('사용할 수 없는 이메일입니다');
			form.loginId.focus();
			return;
		}
		form.cellphoneNum.value = form.cellphoneNum.value.trim();
		if (form.cellphoneNum.value == 0) {
			alert('전화번호를 입력해주세요');
			return;
		}
		form.address.value = form.cellphoneNum.value.trim();
		if (form.addressName.value == '') {
			alert('주소를 입력해주세요.');
			return;
		}
		submitJoinFormDone = true;
		form.submit();
	}

	
	function checkLoginIdDup(el) {
		const form = $(el).closest('form').get(0);
		
		if (form.loginId.value.length == 0) {
			validLoginId = '';
		}

		var loginId = form.loginId.value;
		var action = './getLoginIdDup';

		$.get(action, {
			isAjax : 'Y',
			loginId : loginId,
		}, function(data) {
			$('.checkDup-msg').text(data.msg);
			if (data.success) {
				$('.checkDup-msg').removeClass('text-red-500');
				validLoginId = data.data1;
			} else {
				$('.checkDup-msg').addClass('text-red-500');
				validLoginId = '';
			}

		}, 'json');
	}
	
	function checkLoginPwConfirm(el) {
		const form = $(el).closest('form').get(0);

		var loginPw = form.loginPw.value;
		var loginPwConfirm = form.loginPwConfirm.value;
		var action = './getLoginPwConfirm';

		$.get(action, {
			isAjax : 'Y',
			loginPw : loginPw,
			loginPwConfirm : loginPwConfirm,
		}, function(data) {
			$('.checkConfirm-msg').text(data.msg);
			if (data.success) {
				$('.checkConfirm-msg').removeClass('text-red-500')
			} else {
				$('.checkConfirm-msg').addClass('text-red-500');
			}

		}, 'json');
	}
	
	function checkEmailDup(el) {
		const form = $(el).closest('form').get(0);
		
		if (form.email.value.length == 0) {
			validEmail = '';
		}
		
		const emailRegex = /\S+@\S+\.\S+/; // 이메일 유효성 검사용 정규표현식
		if (emailRegex.test(form.email.value)) {
			validEmail = '';
		}
		
		var email = form.email.value;
		var action = './getEmailDup';
		
		$.get(action, {
			isAjax : 'Y',
			email : email,
		}, function(data) {
			$('.checkEmailDup-msg').text(data.msg);
			if (data.success) {
				$('.checkEmailDup-msg').removeClass('text-red-500');
				validEmail = data.data1;
			} else {
				$('.checkEmailDup-msg').addClass('text-red-500');
				validEmail = '';
			}
		}, 'json');
	}
	
	function kakaoMapPost(longitude, latitude, name, address) {
		  var action = './doCheckData';

		  $.get(action, {
		    isAjax: 'Y',
		    longitude: longitude,
		    latitude: latitude,
		    name: name,
		    address: address
		  }, function(data) {
			$('.longitude').val(data.data1[0]);
			$('.latitude').val(data.data1[1]);
		    $('.addressName').text(data.data1[2]);
		    $('.inputAddressName').val(data.data1[2]);
		    $('.address').text(data.data1[3]);
		    $('.inputAddress').val(data.data1[3]);
		  }, 'json');
		}
</script>

<!-- 조인 폼 -->
<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<form class="table-box-type-1" name="form1" method="POST" action="../member/doJoin" onsubmit="submitJoinForm(this); return false;">
			<input type="hidden" name="afterLoginUri" value="${param.afterLoginUri}" />
			<input class="inputAddressName" type="hidden" name="addressName" />
			<input class="inputAddress" type="hidden" name="address" />
			<input class="longitude" type="hidden" name="longitude" />
			<input class="latitude" type="hidden" name="latitude" />
			<table class="table table-zebra">
				<colgroup>
					<col width="200" />
					<col width="400" />
				</colgroup>
					<tr>
						<th>아이디</th>
						<td>
							<input onblur="checkLoginIdDup(this);" name="loginId" class="w-full input input-bordered  max-w-xs" placeholder="아이디를 입력해주세요" />
							<div class="checkDup-msg mt-2">&nbsp</div>
						</td>
					</tr>
					<tr>
						<th>비밀번호</th>
						<td>
							<input onblur="checkLoginPwConfirm(this);" name="loginPw" class="w-full input input-bordered  max-w-xs" placeholder="비밀번호를 입력해주세요" />
						</td>
					</tr>
					<tr>
						<th>비밀번호 확인</th>
						<td>
							<input onblur="checkLoginPwConfirm(this);" name="loginPwConfirm" class="w-full input input-bordered  max-w-xs" placeholder="비밀번호 확인을 입력해주세요" />
							<div class="checkConfirm-msg mt-2">&nbsp</div>
						</td>
					</tr>
					<tr>
						<th>이름</th>
						<td>
							<input name="name" class="w-full input input-bordered  max-w-xs" placeholder="이름을 입력해주세요" />
						</td>
					</tr>
					<tr>
						<th>닉네임</th>
						<td>
							<input name="nickname" class="w-full input input-bordered  max-w-xs" placeholder="닉네임을 입력해주세요" />
						</td>
					</tr>
					<tr>
						<th>전화번호</th>
						<td>
							<input name="cellphoneNum" class="w-full input input-bordered  max-w-xs" placeholder="전화번호를 입력해주세요" />
						</td>
					</tr>
					<tr>
						<th>이메일</th>
						<td>
							<input onblur="checkEmailDup(this);" name="email" class="w-full input input-bordered  max-w-xs" placeholder="이메일을 입력해주세요" />
							<div class="checkEmailDup-msg mt-2">&nbsp</div>
						</td>
					</tr>
					<tr>
						<th>주소</th>
						<td class="text-left"> <div class="addressName"></div> <div class="address"></div>
								<a class="btn btn-active btn-ghost text-xl" href="#"
									onclick="window.open('/usr/home/kakaoMap', '장소 검색','width=900, height=550'); return false">장소 검색</a>
						</td>
					</tr>
					<tr>
						<th><button class="btn-text-link btn btn-active btn-ghost text-xl" type="button" onclick="history.back();">뒤로가기</button></th>
						<td>
							<button class="btn btn-active btn-ghost text-xl" type="submit" value="회원가입">회원가입</button>
						</td>
					</tr>
			</table>
		</form>
	</div>
</section>

<%@ include file="../common/foot.jspf"%>