<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
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
	background-color: #000;
	width: 500px;
	height: 500px;
	overflow: auto;
}

.chating .me {
	color: #F6F6F6;
	text-align: right;
}

.chating .notice {
	color: #FFE400;
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
			
			$.get('./load-chat-message', {
				relId : "${roomNumber}"
			}, function(data) {
		        
		        // chatMessages를 활용하여 원하는 작업을 수행합니다.
		        for (var i = 0; i < data.length; i++) {
		            var message = data[i].message;
		            console.log(message); // 콘솔에 채팅 메시지 출력 예시
		            
		            if (data[i].userName.localeCompare("${rq.loginedMember.nickname}") == 0) {
		            	$("#chating").append(
								"<p class='me'>나 :" + data[i].message + "</p>");
		            } else {
		            	$("#chating").append(
								"<p class='others'>" + data[i].userName + " :"
										+ data[i].message + "</p>");
		            }
		        }
		        
		        enterSend(nickname);
			}, 'json');
		}

		ws.onmessage = function(data) {
			//메시지를 받으면 동작
			var msg = data.data;
			if (msg != null && msg.trim() != '') {
				var d = JSON.parse(msg);
				if (d.type == "getId") {
					var si = d.sessionId != null ? d.sessionId : "";
					if (si != '') {
						$("#sessionId").val(si);
					}
				} else if (d.type == "message") {
					//메시지 보내기
					if (d.sessionId == $("#sessionId").val()) {
						$("#chating").append(
								"<p class='me'>" + d.userName + " :"
										+ d.msg + "</p>");
						
						/*
						//메시지 데이터베이스에 저장 (내가 보낸 것만 저장)
						$.get('./save-chat-message', {
							isAjax : 'Y',
							message : d.msg,
							userName : d.userName,
							relId : "${roomNumber}"
						}, function(data) {
							console.log("성공");
						}, 'json');
						*/
						
					} else {
						$("#chating").append(
								"<p class='others'>" + d.userName + " :"
										+ d.msg + "</p>");
					}
					
					
					
				} else if (d.type == "enterNotice") {
					$("#chating").append(
							"<p class='notice'>" + d.userName
									+ "님이 입장하셨습니다.</p>");
				} else if (d.type == "exitNotice") {
					$("#chating").append(
							"<p class='notice'>" + d.userName
									+ "님이 퇴장하셨습니다.</p>");
				} else {
					console.warn("unknown type!")
				}
			}
			
			
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
			alert("사용자 이름을 입력해주세요.");
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
			userName : name,
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
</script>
<body>
	<div id="container" class="container">
		<h1>글 제목 : ${roomName}</h1>
		<input type="hidden" id="sessionId" value=""> <input
			type="hidden" id="roomNumber" value="${roomNumber}">

		<div id="chating" class="chating"></div>

		<div id="yourMsg">
			<table class="inputTable">
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