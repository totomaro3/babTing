package com.babTing.toto.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
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
	
	@Select("""
			<script>
			SELECT roomNumber, RoomName
			FROM chatRoom AS CR
			INNER JOIN ChatParticipants AS CP
			ON userName = #{nickname} AND CR.roomNumber = CP.relId;
			</script>
			""")
	List<Room> getMyRoomList(String nickname);

	@Insert("""
			<script>
			INSERT INTO chatMessage
			SET regDate = NOW(),
			updateDate = NOW(),
			`type` = #{type},
			message = #{message},
			userName = #{userName},
			relId = #{relId}
			</script>
			""")
	void save(String type, String message, String userName, int relId);

	@Select("""
			<script>
			SELECT *
			FROM chatMessage
			WHERE relId = #{relId}
			</script>
			""")
	List<ChatMessage> load(int relId);

	@Insert("""
			INSERT INTO chatRoom
			SET regDate = NOW(),
			updateDate = NOW(),
			roomNumber = #{id},
			roomName = #{title};
			""")
	void write(int id, String title);

	@Insert("""
			INSERT INTO ChatParticipants
			SET regDate = NOW(),
			updateDate = NOW(),
			userName = #{userName},
			relId = #{relId}
			""")
	void addChatParticipant(String userName, int relId);

	@Delete("""
			DELETE FROM ChatParticipants
			WHERE userName = #{userName} AND relId = #{relId}
			""")
	void delChatParticipant(String userName, int relId);
}