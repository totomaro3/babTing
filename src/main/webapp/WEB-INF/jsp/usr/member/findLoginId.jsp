<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="MEMBER FINDLOGINID" />
<%@ include file="../common/head.jspf"%>
<hr />

<script type="text/javascript">
	let MemberFindLoginId__submitFormDone = false;

	function MemberFindLoginId__submit(form) {
		if (MemberFindLoginId__submitFormDone) {
			return;
		}

		form.name.value = form.name.value.trim();
		form.email.value = form.email.value.trim();

		if (form.name.value.length == 0) {
			alert('이름 써라');
			form.name.focus();
			return;
		}

		if (form.email.value.length == 0) {
			alert('email 써라');
			form.email.focus();
			return;
		}

		MemberFindLoginId__submitFormDone = true;
		form.submit();

	}
</script>

<section class="mt-5 text-xl">
	<div class="container mx-auto px-3">
		<form method="post" action="doFindLoginId"
			onsubmit="MemberFindLoginId__submit(this); return false;">
			<input type="hidden" name="afterFindLoginIdUri"
				value="${param.afterFindLoginIdUri }" />
			<table>
				<tr>
					<th>이름</th>
					<td><input class="input input-bordered w-full max-w-xs"
						type="text" name="name" placeholder="이름을 입력해주세요" /></td>
				</tr>
				<tr>
					<th>이메일</th>
					<td><input class="input input-bordered w-full max-w-xs"
						type="text" name="email" placeholder="이메일을 입력해주세요" /></td>
				</tr>
				<tr>
					<th><button class="button btn btn-active btn-ghost text-xl"
							type="button" onclick="history.back();">뒤로가기</button></th>
					<td><button class="btn btn-active btn-ghost text-xl"
							type="submit">찾기</button></td>
				</tr>
			</table>
		</form>
	</div>
</section>

<%@ include file="../common/foot.jspf"%>