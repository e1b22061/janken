package oit.is.z2620.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchInfoMapper {
  @Insert("INSERT INTO matchinfo (user1, user2, user1Hand, isActive) VALUES (#{user1}, #{user2}, #{user1Hand}, #{isActive})")
  void insertMatchInfo(MatchInfo matchInfo);

  @Select("SELECT * FROM matchinfo WHERE id = #{id}")
  MatchInfo selectMatchInfoById(int id);

  @Select("SELECT * FROM matchinfo WHERE isActive = true")
  ArrayList<MatchInfo> selectActiveMatchInfo();

  @Select("SELECT * FROM matchinfo WHERE isActive = true AND user1 = #{opponentId} AND user2 = #{playerId}")
  ArrayList<MatchInfo> selectActiveMatchInfoById(int opponentId, int playerId);

  @Update("UPDATE matchinfo SET isActive = false WHERE id = #{id}")
  void updateMatchInfo(int id);

}
