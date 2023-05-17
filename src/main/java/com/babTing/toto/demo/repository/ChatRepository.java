package com.babTing.toto.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.babTing.toto.demo.vo.Room;

@Mapper
public interface ChatRepository {

	@Select("""
			<script>
			SELECT roomNumber, RoomName
			FROM chatRoom
			</script>
			""")
	List<Room> getRooms();

	
	
}