<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Find LoginPw" />
<%@ include file="../common/head.jspf"%>
<hr />

<!-- 빈 칸 체크 -->
<script type="text/javascript">
	let MemberFindLoginPw__submitFormDone = false;

	function MemberFindLoginPw__submit(form) {
		if (MemberFindLoginPw__submitFormDone) {
			return;
		}

		form.name.value = form.name.value.trim();
		form.email.value = form.email.value.trim();

		if (form.name.value.length == 0) {
			alert('아이디를 입력해주세요.');
			form.name.focus();
			return;
		}

		if (form.email.value.length == 0) {
			alert('이메일을 입력해주세요.');
			form.email.focus();
			return;
		}

		MemberFindLoginPw__submitFormDone = true;
		form.submit();

	}
</script>

<!-- 비밀번호 찾기 폼 -->
<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1">
			<form action="../member/doFindLoginPw" method="POST" onsubmit="MemberFindLoginPw__submit(this); return false;">
				<input type="hidden" name="afterFindLoginPwUri" value="${param.afterFindLoginPwUri }" />
				<table border="1">
					<colgroup>
						<col width="200" />
					</colgroup>

					<tbody>
						<tr>
							<th>아이디</th>
							<td>
								<input class="input input-bordered w-full max-w-xs" autocomplete="off" type="text" placeholder="이름을 입력해주세요"
									name="loginId" />
							</td>
						</tr>
						<tr>
							<th>이메일</th>
							<td>
								<input class="input input-bordered w-full max-w-xs" autocomplete="off" type="text" placeholder="이메일을 입력해주세요"
									name="email" />
							</td>
						</tr>
						<tr>
							<th>
								<button class="btn-text-link btn btn-active btn-ghost text-xl" type="button" onclick="history.back();">뒤로가기</button>
							</th>
							<td>
								<button class="btn btn-active btn-ghost text-xl" type="submit">비밀번호 찾기</button>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jspf"%>