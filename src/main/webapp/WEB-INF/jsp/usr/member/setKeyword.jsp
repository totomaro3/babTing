<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="MEMBER MYPAGE" />
<%@ include file="../common/head.jspf"%>

	
<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
	<h1 class="text-3xl">맞춤 밥팅 키워드 설정</h1>
	<h1 class="text-3xl">최대 5개 까지 설정할 수 있습니다.</h1>
		<form method="post" action="doSetKeyword"
			onsubmit="MemberSetKeyword__submit(this); return false;">
			<input type="hidden" name="id" value="${rq.loginedMember.id }" />
			<table>
				<tr>
					<th>키워드1</th>
					<td><input class="input input-bordered w-full max-w-xs"
						type="text" name="keyword1" value="${rq.loginedMember.keyword1 }" placeholder="검색 키워드를 입력해주세요" /></td>
				</tr>
				<tr>
					<th>키워드2</th>
					<td><input class="input input-bordered w-full max-w-xs"
						type="text" name="keyword2" value="${rq.loginedMember.keyword2 }" placeholder="검색 키워드를 입력해주세요" /></td>
				</tr>
				<tr>
					<th>키워드3</th>
					<td><input class="input input-bordered w-full max-w-xs"
						type="text" name="keyword3" value="${rq.loginedMember.keyword3 }" placeholder="검색 키워드를 입력해주세요" /></td>
				</tr>
				<tr>
					<th>키워드4</th>
					<td><input class="input input-bordered w-full max-w-xs"
						type="text" name="keyword4" value="${rq.loginedMember.keyword4 }" placeholder="검색 키워드를 입력해주세요" /></td>
				</tr>
				<tr>
					<th>키워드5</th>
					<td><input class="input input-bordered w-full max-w-xs"
						type="text" name="keyword5" value="${rq.loginedMember.keyword5 }" placeholder="검색 키워드를 입력해주세요" /></td>
				</tr>
				<tr>
					<th><button class="button btn btn-active btn-ghost text-xl"
							type="button" onclick="history.back();">뒤로가기</button></th>
					<td><button class="btn btn-active btn-ghost text-xl"
							type="submit">설정</button></td>
				</tr>
			</table>
		</form>
	</div>
</section>

<%@ include file="../common/foot.jspf"%>