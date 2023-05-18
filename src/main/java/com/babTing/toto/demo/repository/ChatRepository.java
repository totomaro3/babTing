package com.babTing.toto.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.babTing.toto.demo.vo.ChatMessage;
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
	
	
	@Insert("""
			<script>
			INSERT INTO chatMessage
			SET regDate = NOW(),
			updateDate = NOW(),
			message = #{message},
			userName = #{userName},
			relId = #{relId}
			</script>
			""")
	void save(String message, String userName, int relId);

	@Select("""
			<script>
			SELECT message, userName
			FROM chatMessage 
			</script>
			""")
	List<ChatMessage> load();
}