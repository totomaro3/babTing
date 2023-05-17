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
import com.babTing.toto.demo.vo.Room;

@Controller
public class UsrChatController {

	@Autowired
	private ChatService chatService;

	private boolean isInit = false;

	List<Room> roomList = new ArrayList<Room>();
	static int roomNumber = 0;

	@RequestMapping("/usr/chat/chat")
	public ModelAndView chat() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/usr/chat/chat");
		return mv;
	}

	/**
	 * 방 페이지
	 * 
	 * @return
	 */
	@RequestMapping("/usr/chat/room")
	public ModelAndView room() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/usr/chat/room");
		return mv;
	}

	/**
	 * 방 생성하기
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/usr/chat/createRoom")
	public @ResponseBody List<Room> createRoom(@RequestParam HashMap<Object, Object> params) {

		List<Room> rooms = chatService.getRooms();

		for (Room room : rooms) {
				roomList.add(room);
		}

		return roomList;
	}

	/**
	 * 방 최초 생성하기
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/usr/chat/initCreateRoom")
	public @ResponseBody List<Room> initCreateRoom(@RequestParam HashMap<Object, Object> params) {

		if (!isInit) {
			List<Room> rooms = chatService.getRooms();

			for (Room room : rooms) {
					roomList.add(room);
			}
			
			isInit = true;
		}
		return roomList;
	}

	/**
	 * 방 정보가져오기
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/usr/chat/getRoom")
	public @ResponseBody List<Room> getRoom(@RequestParam HashMap<Object, Object> params) {
		return roomList;
	}

	/**
	 * 채팅방
	 * 
	 * @return
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
}