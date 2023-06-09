<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="ARTICLE WRITE" />
<%@ include file="../common/head.jspf"%>
<%@ include file="../common/toastUiEditorLib.jspf"%>
<hr />

<!-- 빈 칸 체크 -->
<script type="text/javascript">
	let ArticleWrite__submitFormDone = false;

	function ArticleWrite__submit(form) {
		if (ArticleWrite__submitFormDone) {
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
			alert('내용 써라');
			editor.focus();
			return;
		}

		form.body.value = markdown;

		ArticleWrite__submitFormDone = true;
		form.submit();

	}
</script>

<!-- 장소 작성 -->
<script>
	function kakaoMapPost(longitude, latitude, name, address, category) {
		  var action = '/usr/member/doCheckData';

		  $.get(action, {
		    isAjax: 'Y',
		    longitude: longitude,
		    latitude: latitude,
		    name: name,
		    address: address,
		    category: category
		  }, function(data) {
			$('.longitude').val(data.data1[0]);
			$('.latitude').val(data.data1[1]);
			$('.inputName').val(data.data1[2]);
			$('.inputAddress').val(data.data1[3]);
		    $('.name').text(data.data1[2]);
		    $('.address').text(data.data1[3]);
		    $('.inputCategory').val(data.data1[4]);
		  }, 'json');
		}
</script>

<!-- 작성 폼 -->
<section class="mt-8 text-3xl">
	<div class="container mx-auto px-3">
		<div class="table-box-type-1">
			<form action="../article/doWrite" method="POST"
				onsubmit="ArticleWrite__submit(this); return false;">
				<input type="hidden" name="body">
				<input type="hidden" name="boardId" value = "${param.boardId }">
				<input class="inputName" type="hidden" name="restaurantName"/>
				<input class="inputAddress" type="hidden" name="address"/>
				<input class="inputCategory" type="hidden" name="category"/>
				<input class="longitude" type="hidden" name="longitude" />
				<input class="latitude" type="hidden" name="latitude" />
				<table>
					<colgroup>
						<col width="300" />
					</colgroup>

					<tbody>
						<tr>
							<th>작성자</th>
							<td>
								<div class="text-left">${rq.loginedMember.nickname }</div>
							</td>
						</tr>
						<tr>
							<th>게시판</th>
							<td class="text-left">
								<select name="boardId"
								class="select select-bordered w-full max-w-xs" disabled>
									<option value="1" ${param.boardId == 1 ? "selected" : ""}>소개</option>
									<option value="2" ${param.boardId == 2 ? "selected" : ""}>밥팅</option>
									<option value="3" ${param.boardId == 3 ? "selected" : ""}>자유</option>
									<option value="4" ${param.boardId == 4 ? "selected" : ""}>문의</option>
								</select>
							</td>
						</tr>
						<c:if test="${param.boardId == 2}">
							<tr>
								<th>매장</th>
								<td class="text-left"> <div class="name"></div> <div class="address"></div>
								<a class="btn btn-active btn-ghost text-xl" href="#"
									onclick="window.open('/usr/home/kakaoMap', '장소 검색','width=900, height=550'); return false">장소 검색</a>
								</td>
							</tr>
							<tr>
								<th>예상 배달 비용</th>
								<td class="text-left"><input
									class="input input-bordered w-full max-w-xs"
									value="${article.deliveryCost }" type="text" name="deliveryCost"
									placeholder="예상 배달 비용" /></td>
							</tr>
						</c:if>
						<tr>
							<th>제목</th>
							<td><input class="w-full rounded-sm" type="text"
								name="title" placeholder="제목을 입력해주세요" /></td>
						</tr>
						<tr>
							<th>내용</th>
							<td>
								<div class="toast-ui-editor text-left">
									<script type="text/x-template"></script>
								</div>
							</td>
						</tr>
						<tr>
							<th><button
									class="btn-text-link btn btn-active btn-ghost btn-lg"
									type="button" onclick="history.back();">뒤로가기</button></th>
							<td>
								<button class="btn-text-link btn btn-active btn-ghost btn-lg"
									type="submit" value="작성" /> 작성
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