<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<!-- 테일윈드 불러오기 -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/tailwindcss/2.2.19/tailwind.min.css" />

<!-- Daisy UI -->
<link href="https://cdn.jsdelivr.net/npm/daisyui@2.51.5/dist/full.css"
	rel="stylesheet" type="text/css" />
<script src="https://cdn.tailwindcss.com"></script>

<!-- 폰트어썸 불러오기 -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css">

<!-- 제이쿼리 불러오기 -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>

<!-- 로대쉬 불러오기 -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.21/lodash.min.js"></script>

<link rel="stylesheet" href="/resource/common.css" />
<link rel="shortcut icon" href="/resource/favicon.ico" />
<script src="/resource/common.js" defer="defer"></script>


<script type="text/javascript"
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=APIKEY&libraries=LIBRARY"></script>
<!-- services 라이브러리 불러오기 -->
<script type="text/javascript"
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=APIKEY&libraries=services"></script>
<!-- services와 clusterer, drawing 라이브러리 불러오기 -->
<script type="text/javascript"
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=APIKEY&libraries=services,clusterer,drawing"></script>

<meta charset="UTF-8">
<title>Chating</title>
<style>
* {
	margin: 0;
	padding: 0;
}

.container {
	width: 500px;
	margin: 0 auto;
	padding: 25px
}

.container h1 {
	text-align: left;
	padding: 5px 5px 5px 15px;
	margin-bottom: 20px;
}

.chating {
	background-color: #E0EBED;
	width: 550px;
	height: 600px;
	overflow: auto;
}

.chating .me {
	color: #F6F6F6;
	text-align: right;
}

.chating .notice {
	color: #000000;
	text-align: center;
}

.chating .others {
	color: #FFE400;
	text-align: left;
}

input {
	width: 330px;
	height: 25px;
}

#yourMsg {
	display: none;
}
</style>
</head>

<script type="text/javascript">
	var ws;
	var nickname = "${rq.loginedMember.nickname}";

	function wsOpen() {
		//웹소켓 전송시 현재 방의 번호를 넘겨서 보낸다.
		ws = new WebSocket("ws://" + location.host + "/chating/"
				+ $("#roomNumber").val());
		wsEvt();
	}

	function wsEvt() {
		ws.onopen = function(data) {
			//소켓이 열리면 동작
			var nickname = "${rq.loginedMember.nickname}";
			$
					.get(
							'./load-chat-message',
							{
								relId : "${roomNumber}"
							},
							function(data) {
								// chatMessages를 활용하여 원하는 작업을 수행합니다.
								for (var i = 0; i < data.length; i++) {
									var message = data[i].message;
									console.log(message); // 콘솔에 채팅 메시지 출력 예시
									if (data[i].userName.localeCompare('') == 0) {
										$("#chating").append(
												"<p class='notice'>"
														+ data[i].message
														+ "</p>");
									} else if (data[i].userName
											.localeCompare("${rq.loginedMember.nickname}") == 0) {
										$("#chating")
												.append(
														"<div class='chat chat-end'><div class='chat-header'>"
																+ data[i].userName
																+ "&nbsp<time class='text-xs opacity-50'>"
																+ data[i].regDate
																		.substring(
																				10,
																				16)
																+ "</time></div><div class='chat-bubble chat-bubble-warning'>"
																+ data[i].message
																+ "</div></div>");
									} else {
										$("#chating")
												.append(
														"<div class='chat chat-start'> <div class='chat-header'>"
																+ data[i].userName
																+ "&nbsp<time class='text-xs opacity-50'>"
																+ data[i].regDate
																		.substring(
																				10,
																				16)
																+ "</time></div><div class='chat-bubble chat-bubble-success'>"
																+ data[i].message
																+ "</div></div>");
									}
								}

								enterSend(nickname);
							}, 'json');
		}

		ws.onmessage = function(data) {
			//메시지를 받으면 동작
			var msg = data.data;
			console.log(data);
			if (msg != null && msg.trim() != '') {
				var d = JSON.parse(msg);
				if (d.type == "getId") {
					var si = d.sessionId != null ? d.sessionId : "";
					if (si != '') {
						$("#sessionId").val(si);
					}
				} else if (d.type == "message") {
					
					//현재날짜 구하기
					var currentDate = new Date();
					var formattedTime = currentDate.getHours() + ':'
							+ currentDate.getMinutes();
					var regDate = formattedTime;
					
					//메시지 보내기
					if (d.sessionId == $("#sessionId").val()) {
						$("#chating")
								.append(
										"<div class='chat chat-end'><div class='chat-header'>"
												+ d.userName
												+ "&nbsp<time class='text-xs opacity-50'>"
												+ regDate
												+ "</time></div><div class='chat-bubble chat-bubble-warning'>"
												+ d.msg + "</div></div>");

						//메시지 데이터베이스에 저장 (내가 보낸 것만 저장)
						$.get('./save-chat-message', {
							isAjax : 'Y',
							message : d.msg,
							userName : d.userName,
							relId : "${roomNumber}"
						}, function(data) {
							console.log("성공");
						}, 'json');

					} else {
						$("#chating")
								.append(
										"<div class='chat chat-start'><div class='chat-header'>"
												+ d.userName
												+ "&nbsp<time class='text-xs opacity-50'>"
												+ regDate
												+ "</time></div><div class='chat-bubble chat-bubble-success'>"
												+ d.msg + "</div></div>");
					}

				} else if (d.type == "enterNotice") {
					$("#chating").append(
							"<p class='notice'>" + d.userName
									+ "님이 입장하셨습니다.</p>");

					//메시지 데이터베이스에 저장 (입장)
					$.get('./save-chat-message', {
						isAjax : 'Y',
						message : d.userName + '님이 입장하셨습니다.',
						userName : '',
						relId : "${roomNumber}"
					}, function(data) {
						console.log("성공");
					}, 'json');

				} else if (d.type == "exitNotice") {
					$("#chating").append(
							"<p class='notice'>" + d.userName
									+ "님이 퇴장하셨습니다.</p>");

					//메시지 데이터베이스에 저장 (퇴장)
					$.get('./save-chat-message', {
						isAjax : 'Y',
						message : d.userName + '님이 퇴장하셨습니다.',
						userName : '',
						relId : "${roomNumber}"
					}, function(data) {
						console.log("성공");
					}, 'json');

				} else {
					console.warn("unknown type!")
				}
			}
			scrollToBottom();
		}

		ws.onclose = function(data) {
			//소켓이 닫히면 동작
			Console.log("동작?");
			var nickname = "${rq.loginedMember.nickname}";
			exitSend(nickname);
		}

		document.addEventListener("keypress", function(e) {
			if (e.keyCode == 13) { //enter press
				send();
			}
		});
	}

	window.onload = function chatName() {
		var userName = '${rq.loginedMember.nickname}';
		if (userName == null || userName.trim() == "") {
			alert("대화방에 참여하시려면 로그인을 해주세요.");
			window.close();
		} else {
			wsOpen();
			$("#yourMsg").show();
		}
	}

	function send(name) {
		var option = {
			type : "message",
			roomNumber : $("#roomNumber").val(),
			sessionId : $("#sessionId").val(),
			userName : nickname,
			msg : $("#chatting").val()
		}
		ws.send(JSON.stringify(option))
		$('#chatting').val("");
	}

	function enterSend(name) {
		var option = {
			type : "enterNotice",
			roomNumber : $("#roomNumber").val(),
			sessionId : $("#sessionId").val(),
			userName : name,
			msg : "회원이 입장했습니다."
		}

		ws.send(JSON.stringify(option))
	}

	function exitSend(name) {
		var option = {
			type : "exitNotice",
			roomNumber : $("#roomNumber").val(),
			sessionId : $("#sessionId").val(),
			userName : name,
			msg : "회원이 퇴장했습니다."
		}

		ws.send(JSON.stringify(option))
		ws.close();
		window.close();
	}
	
	function scrollToBottom() {
		  var chatContainer = document.querySelector('.chating');
		  chatContainer.scrollTop = chatContainer.scrollHeight;
		}
</script>
<body>
	<div id="container" class="container">
		<div class="text-2xl flex justify-center">글 제목 : ${roomName}</div>
		<br> <input type="hidden" id="sessionId" value=""> <input
			type="hidden" id="roomNumber" value="${roomNumber}">

		<div class="flex justify-center">
			<div id="chating" class="chating"></div>
		</div>


		<div id="yourMsg">
			<table class="inputTable flex justify-center">
				<tr>
					<th>메시지</th>
					<th><input id="chatting" placeholder="보내실 메시지를 입력하세요."></th>
					<th><button onclick="send('${rq.loginedMember.nickname}')"
							id="sendBtn">보내기</button></th>
					<th><button onclick="exitSend('${rq.loginedMember.nickname}')"
							id="sendBtn">나가기</button></th>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>