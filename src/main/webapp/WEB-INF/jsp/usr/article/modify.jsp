<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="ARTICLE MODIFY" />
<%@ include file="../common/head.jspf"%>
<%@ include file="../common/toastUiEditorLib.jspf"%>
<hr />

<!-- 빈 칸 체크 -->
<script type="text/javascript">
	let ArticleModify__submitFormDone = false;

	function ArticleModify__submit(form) {
		if (ArticleModify__submitFormDone) {
			return;
		}

		form.title.value = form.title.value.trim();
		if (form.title.value == 0) {
			alert('제목을 입력해주세요');
			return;
		}

		const editor = $(form).find('.toast-ui-editor').data(
				'data-toast-editor');
		const markdown = editor.getMarkdown().trim();

		if (markdown.length == 0) {
			alert('내용을 입력해주세요');
			editor.focus();
			return;
		}

		form.body.value = markdown;

		ArticleModify__submitFormDone = true;
		form.submit();

	}
</script>

<!-- 장소 수정 -->
<script>
	function kakaoMapPost(longitude, latitude, name, address, category) {
		var action = '../member/doCheckData';

		$.get(action, {
			isAjax : 'Y',
			longitude : longitude,
			latitude : latitude,
			name : name,
			address : address,
			category : category
		}, function(data) {
			$('.longitude').val(data.data1[0]);
			$('.latitude').val(data.data1[1]);
			$('.inputName').val(data.data1[2]);
			$('.name').text(data.data1[2]);
			$('.inputAddress').val(data.data1[3]);
			$('.address').text(data.data1[3]);
			$('.inputCategory').val(data.data1[4]);
		}, 'json');
	}
</script>

<!-- 수정 폼 -->
<section class="mt-8">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1">
			<form action="../article/doModify" method="POST" onsubmit="ArticleModify__submit(this); return false;">
				<input type="hidden" name="body" />
				<input type="hidden" name="id" value="${article.id }" />
				<input type="hidden" name="boardId" value="${article.boardId }"/>
				<input type="hidden" name="restaurantName" value="${article.restaurantName }" class="inputName"/>
				<input type="hidden" name="address" value="${article.address }" class="inputAddress"/>
				<input type="hidden" name="category" value="${article.category }" class="inputCategory"/>
				<input type="hidden" name="longitude" value="${article.longitude }" class="longitude"/>
				<input type="hidden" name="latitude" value="${article.latitude }" class="latitude"/>
				<table>
					<colgroup>
						<col width="200" />
						<col width="600" />
					</colgroup>

					<tbody>
						<c:if test="${article.boardId == 1 || article.boardId == 3}">
							<tr>
								<th>번호</th>
								<td>
									<div class="badge">${article.id}</div>
								</td>
							</tr>
						</c:if>
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
								<th>매장</th>
								<td class="text-left">
									<div class="name">${article.restaurantName }</div>
									<div class="address">${article.address }</div> <a
									class="btn btn-active btn-ghost text-xl" href="#"
									onclick="window.open('/usr/home/kakaoMap', '장소 수정','width=900, height=550'); return false">장소
										수정</a>
								</td>
							</tr>
							<tr>
								<th>예상 배달 비용</th>
								<td class="text-left"><input
									class="input input-bordered w-full max-w-xs"
									value="${article.deliveryCost }" type="text"
									name="deliveryCost" placeholder="예상 배달 비용" /></td>
							</tr>
						</c:if>
						<tr>
							<th>제목</th>
							<td><input class="w-full rounded-sm" type="text"
								name="title" placeholder="제목을 입력해주세요" value="${article.title }" /></td>
						</tr>
						<tr>
							<th>내용</th>
							<td>
								<div class="toast-ui-editor text-left">
									<script class="text-lg" type="text/x-template">${article.body }</script>
								</div>
							</td>
						</tr>
						<tr>
							<th><button
									class="btn-text-link btn btn-active btn-ghost text-xl"
									type="button" onclick="history.back();">뒤로가기</button></th>
							<td>
								<button class="btn-text-link btn btn-active btn-ghost text-xl"
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