package com.babTing.toto.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.babTing.toto.demo.service.ChatService;
import com.babTing.toto.demo.vo.ChatMessage;
import com.babTing.toto.demo.vo.ResultData;
import com.babTing.toto.demo.vo.Room;
import com.babTing.toto.demo.vo.Rq;

@Controller
public class UsrChatController {

	@Autowired
	private ChatService chatService;
	@Autowired
	private Rq rq;

	private boolean isInit = false;

	List<Room> roomList = new ArrayList<Room>();
	static int roomNumber = 0;
	
	/**
	 * 채팅방 불러오기 (moveChating이 대신해줌 사용x)
	 * @return 채팅 이동
	 */
	@RequestMapping("/usr/chat/chat")
	public ModelAndView chat() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/usr/chat/chat");
		return mv;
	}

	/**
	 * 방 보기
	 * @return 방 이동
	 */
	@RequestMapping("/usr/chat/room")
	public ModelAndView room() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/usr/chat/room");
		return mv;
	}
	
	/**
	 * 방 보기에서 채팅방 목록 (테스트 때만 사용) Ajax
	 * @param params
	 * @return roomList
	 */
	@RequestMapping("/usr/chat/getRoom")
	public @ResponseBody List<Room> getRoom(@RequestParam HashMap<Object, Object> params) {
			
		List<Room> myRoomList = chatService.getMyRoomList(rq.getLoginedMember().getNickname());

		return myRoomList;
	}

	/**
	 * 글 작성 시 채팅방 만들기 Ajax (사용 안함)
	 * @param params
	 * @return roomList
	 */
	@RequestMapping("/usr/chat/createRoom")
	public @ResponseBody List<Room> createRoom(@RequestParam HashMap<Object, Object> params) {

		roomList = new ArrayList<Room>();
		
		List<Room> rooms = chatService.getRooms();

		for (Room room : rooms) {
			roomList.add(room);
		}

		return roomList;
	}

	/**
	 * 데이터베이스에 저장되어 있는 채팅방 웹페이지에 만들기 Ajax
	 * @param params
	 * @return roomList
	 */
	@RequestMapping("/usr/chat/initCreateRoom")
	public @ResponseBody List<Room> initCreateRoom(@RequestParam HashMap<Object, Object> params) {

			roomList = new ArrayList<Room>();
		
			List<Room> rooms = new ArrayList<Room>();
			
			rooms = chatService.getRooms();

			for (Room room : rooms) {
				roomList.add(room);
			}
		
		return roomList;
	}

	/**
	 * 채팅방으로 이동
	 * @param params
	 * @return 채팅방 이동
	 */
	@RequestMapping("/usr/chat/moveChating")
	public ModelAndView chating(@RequestParam HashMap<Object, Object> params) {
		ModelAndView mv = new ModelAndView();
		int roomNumber = Integer.parseInt((String) params.get("roomNumber"));

		List<Room> new_list = roomList.stream().filter(o -> o.getRoomNumber() == roomNumber)
				.collect(Collectors.toList());
		if (new_list != null && new_list.size() > 0) {
			mv.addObject("roomName", params.get("roomName"));
			mv.addObject("roomNumber", params.get("roomNumber"));
			mv.setViewName("/usr/chat/chat");
		} else {
			mv.setViewName("/usr/chat/room");
		}
		return mv;
	}
	
	/**
	 * 채팅 내용 데이터 베이스에 저장 Ajax
	 * @param message
	 * @param userName
	 * @param relId
	 * @return
	 */
	@RequestMapping("/usr/chat/save-chat-message")
	@ResponseBody
    public ResultData saveChatMessage(String type, String message, String userName, int relId) {
        // 채팅 메시지를 데이터베이스에 저장하는 로직을 수행합니다.
		chatService.saveChatMessage(type, message, userName, relId);
        return ResultData.from("S-1", "성공");
    }
	
	/**
	 * 저장된 채팅 내용 불러오기 Ajax
	 * @param relId
	 * @return
	 */
	@RequestMapping("/usr/chat/load-chat-message")
	@ResponseBody
    public List<ChatMessage> loadChatMessage(int relId) {
        // 채팅 메시지를 데이터베이스에 저장하는 로직을 수행합니다.
		List<ChatMessage> chatMessages = chatService.getChatMessages(relId);
        return chatMessages;
    }
	
	@RequestMapping("/usr/chat/add-chat-participant")
	@ResponseBody
    public ResultData addChatParticipant(String userName, int relId) {
        // 채팅 메시지를 데이터베이스에 저장하는 로직을 수행합니다.
		chatService.addChatParticipant(userName, relId);
        return ResultData.from("S-1", "성공");
    }
	
	@RequestMapping("/usr/chat/del-chat-participant")
	@ResponseBody
    public ResultData delChatParticipant(String userName, int relId) {
        // 채팅 메시지를 데이터베이스에 저장하는 로직을 수행합니다.
		chatService.delChatParticipant(userName, relId);
        return ResultData.from("S-1", "성공");
    }
}