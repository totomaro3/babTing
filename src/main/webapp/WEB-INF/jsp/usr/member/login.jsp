<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="MEMBER LOGIN" />
<%@ include file="../common/head.jspf"%>

<form method="post" action="doLogin">
	<table>
		<tr>
			<th>아이디</th>
			<td><input class="input input-bordered w-full max-w-xs" type="text" name="loginId" placeholder="아이디를 입력해주세요" /></td>
		</tr>
		<tr>
			<th>비밀번호</th>
			<td><input class="input input-bordered w-full max-w-xs" type="text" name="loginPw" placeholder="비밀번호를 입력해주세요" /></td>
		</tr>
		<tr>
			<th></th>
			<td><button class="btn btn-active btn-ghost text-xl" type="submit">로그인</button></td>
		</tr>
	</table>
</form>
<button class="button btn btn-active btn-ghost text-xl" type="button" onclick="history.back();">뒤로가기</button>

<%@ include file="../common/foot.jspf"%>