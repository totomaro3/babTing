<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="MEMBER LOGIN" />
<%@ include file="../common/head.jspf"%>


<section class="mt-5 text-xl">
	<div class="container mx-auto px-3">
		<form method="post" action="doLogin">
			<input value="${param.afterLoginUri }" type="hidden"
				name="afterLoginUri" />
			<table>
				<tr>
					<th>아이디</th>
					<td><input class="input input-bordered w-full max-w-xs"
						type="text" name="loginId" placeholder="아이디를 입력해주세요" /></td>
				</tr>
				<tr>
					<th>비밀번호</th>
					<td><input class="input input-bordered w-full max-w-xs"
						type="text" name="loginPw" placeholder="비밀번호를 입력해주세요" /></td>
				</tr>
				<tr>
					<th><button class="button btn btn-active btn-ghost text-xl"
							type="button" onclick="history.back();">뒤로가기</button></th>
					<td><button class="btn btn-active btn-ghost text-xl"
							type="submit">로그인</button></td>
				</tr>
				<tr>
					<th><a class="btn btn-active btn-ghost text-xl"
						href="${rq.findLoginIdUri }">아이디 찾기</a></th>
					<td><a class="btn btn-active btn-ghost text-xl"
						href="${rq.findLoginPwUri }">비밀번호 찾기</a></td>
				</tr>
			</table>
		</form>
	</div>
</section>

<%@ include file="../common/foot.jspf"%>