<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="ARTICLE WRITE" />
<%@ include file="../common/head.jspf"%>

<form method="post" action="doWrite">
	<table>
		<tr>
			<th>게시판</th>
			<td>
				<select name="boardId" class="select select-bordered w-full max-w-xs" disabled>
					<option value="1" ${boardId == 1 ? "selected" : ""}>소개</option>
 					<option value="2" ${boardId == 2 ? "selected" : ""}>밥팅</option>
			 		<option value="3" ${boardId == 3 ? "selected" : ""}>자유</option>
			 		<option value="4" ${boardId == 4 ? "selected" : ""}>문의</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>작성자</th>
			<td>${loginedMemberNickname }</td>
		</tr>
	<c:if test="${boardId == 2}">
		<tr>
			<th>매장 이름</th>
			<td><input class="input input-bordered w-full max-w-xs" value="${article.restaurantName }" 
				type="text" name="title" placeholder="시킬 예정인 매장" /></td>
		</tr>
		<tr>
			<th>예상 배달 비용</th>
			<td><input class="input input-bordered w-full max-w-xs" value="${article.deliveryCost }" 
				type="text" name="title" placeholder="예상 배달 비용" /></td>
		</tr>
		<tr>
			<th>모집 마감 시간</th>
			<td>1시간 후</td>
		</tr>
	</c:if>
		<tr>
			<th>제목</th>
			<td><input class="input input-bordered w-full max-w-xs" value="${article.title }" type="text" name="title"
				placeholder="제목을 입력해주세요" /></td>
		</tr>
		<tr>
			<th>내용</th>
			<td><textarea class="input input-bordered w-full max-w-xs" type="text" name="body" placeholder="내용을 입력해주세요" />${article.body }</textarea></td>
		</tr>
		<tr>
			<th><button class="button btn btn-active btn-ghost text-xl" type="button" onclick="history.back();">뒤로가기</button></th>
			<td><button class="button btn btn-active btn-ghost text-xl" type="submit">작성</button></td>
		</tr>
	</table>
	
</form>

<%@ include file="../common/foot.jspf"%>
