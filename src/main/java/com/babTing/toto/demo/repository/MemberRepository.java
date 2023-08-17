package com.babTing.toto.demo.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.babTing.toto.demo.vo.Member;

@Mapper
public interface MemberRepository {

	@Insert("""
			INSERT INTO `member`
			set regDate = NOW(),
			updateDate = NOW(),
			loginId = #{loginId},
			loginPw = #{loginPw},
			`name` = #{name},
			nickname = #{nickname},
			cellphoneNum = #{cellphoneNum},
			email = #{email},
			addressName = #{addressName},
			address = #{address},
			longitude = #{longitude},
			latitude = #{latitude}
			""")
	public void doJoinMember(String loginId, String loginPw, String name, String nickname, String cellphoneNum, String email, String addressName, String address, double longitude, double latitude);

	@Select("""
			SELECT *
			FROM `member`
			WHERE id = #{id}
			""")
	public Member getMemberById(int id);

	@Select("""
			SELECT Count(*)
			FROM `member`
			WHERE loginId = #{loginId}
			""")
	public boolean isDupLoginId(String loginId);

	@Select("""
			SELECT LAST_INSERT_ID()
			""")
	public int getLastInsertId();

	@Select("""
			SELECT Count(*)
			FROM `member`
			WHERE name = #{name} AND email = #{email}
			""")
	public boolean isDupNameAndEmail(String name, String email);
	
	@Select("""
			SELECT *
			FROM `member`
			WHERE name = #{name} AND email = #{email}
			""")
	public Member getMemberByNameAndEmail(String name, String email);
	
	@Select("""
			SELECT loginPw
			FROM `member`
			WHERE name = #{name} AND email = #{email}
			""")
	public String getLoginPwByNameAndEmail(String name, String email);

	@Select("""
			SELECT *
			FROM `member`
			WHERE loginId = #{loginId}
			""")
	public Member getMemberByLoginId(String loginId);

	@Update("""
			<script>
			UPDATE `member`
			<set>
				<if test="loginPw != ''">
					loginPw = #{loginPw},
				</if>
				<if test="name != ''">
					name = #{name},
				</if>
				<if test="nickname != ''">
					nickname = #{nickname},
				</if>
				<if test="cellphoneNum != ''">
					cellphoneNum = #{cellphoneNum},
				</if>
				<if test="email != ''">
					email = #{email},
				</if>
				<if test="addressName != ''">
					addressName = #{addressName},
				</if>
				<if test="address != ''">
					address = #{address},
				</if>
				<if test="longitude != 0">
					longitude = #{longitude},
				</if>
				<if test="latitude != 0">
					latitude = #{latitude},
				</if>
				updateDate= NOW()
			</set>
			WHERE id = #{id}
			</script>
			""")
	public void doModifyMember(int id, String loginPw, String name, String nickname, String cellphoneNum,
			String email, String addressName, String address, double longitude, double latitude);
	
	@Update("""
			<script>
			UPDATE member
			<set>
			delStatus = 1,
			delDate = NOW()
			</set>
			WHERE id = #{id}
			</script>
			""")
	public void doDeleteMember(int id);

	@Update("""
			<script>
			UPDATE member
			<set>
			<if test="keyword1 != ''">
					keyword1 = #{keyword1},
			</if>
			<if test="keyword2 != ''">
					keyword2 = #{keyword2},
			</if>
			<if test="keyword3 != ''">
					keyword3 = #{keyword3},
			</if>
			<if test="keyword4 != ''">
					keyword4 = #{keyword4},
			</if>
			<if test="keyword5 != ''">
					keyword5 = #{keyword5},
			</if>
			</set>
			WHERE id = #{id}
			</script>
			""")
	public void doModifyKeyword(int id, String keyword1, String keyword2, String keyword3, String keyword4, String keyword5);

	@Select("""
			SELECT Count(*)
			FROM `member`
			WHERE email = #{email}
			""")
	public boolean isDupEmail(String email);
}