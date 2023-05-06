package com.babTing.toto.demo.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReactionPointRepository {

	@Select("""
			<script>
				SELECT IFNULL(SUM(RP.point),0)
				FROM reactionPoint AS RP
				WHERE RP.relTypeCode = #{relTypeCode}
				AND RP.relId = #{id}
				AND RP.memberId = #{actorId}
			</script>
			""")
	public int getSumReactionPointByMemberId(int actorId, String relTypeCode, int id);

	@Insert("""
			<script>
				INSERT INTO reactionPoint
				SET regDate = NOW(),
				updateDate = NOW(),
				relTypeCode = #{relTypeCode},
				relId = #{id},
				memberId = #{actorId},
				`point` = 1
			</script>
			""")
	public int addGoodReactionPoint(int actorId, String relTypeCode, int id);

	@Insert("""
			<script>
				INSERT INTO reactionPoint
				SET regDate = NOW(),
				updateDate = NOW(),
				relTypeCode = #{relTypeCode},
				relId = #{id},
				memberId = #{actorId},
				`point` = -1
			</script>
			""")
	public void addBadReactionPoint(int actorId, String relTypeCode, int id);

	@Select("""
			<script>
				SELECT COUNT(*)
				FROM reactionPoint AS RP
				WHERE RP.relTypeCode = #{relTypeCode}
				AND RP.relId = #{id}
				AND RP.memberId = #{actorId}				
				AND RP.point = 1
			</script>
			""")
	public boolean actorHasGoodReaction(int actorId, String relTypeCode, int id);

	@Delete("""
			<script>
				DELETE
				FROM reactionPoint
				WHERE relTypeCode = #{relTypeCode}
				AND relId = #{id}
				AND memberId = #{actorId}				
				AND `point` = 1
			</script>
			""")
	public void delGoodReactionPoint(int actorId, String relTypeCode, int id);

	@Select("""
			<script>
				SELECT COUNT(*)
				FROM reactionPoint AS RP
				WHERE RP.relTypeCode = #{relTypeCode}
				AND RP.relId = #{id}
				AND RP.memberId = #{actorId}				
				AND RP.point = -1
			</script>
			""")
	public boolean actorHasBadReaction(int actorId, String relTypeCode, int id);

	@Delete("""
			<script>
				DELETE
				FROM reactionPoint
				WHERE relTypeCode = #{relTypeCode}
				AND relId = #{id}
				AND memberId = #{actorId}				
				AND `point` = -1
			</script>
			""")
	public void delBadReactionPoint(int actorId, String relTypeCode, int id);
	
	
}