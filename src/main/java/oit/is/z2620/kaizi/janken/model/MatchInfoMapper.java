package oit.is.z2620.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MatchInfoMapper {
  @Insert("INSERT INTO matchinfo (user1, user2, user1_hand, is_active) VALUES (#{user1}, #{user2}, #{user1Hand}, #{isActive})")
  void insertMatchInfo(MatchInfo matchInfo);

  @Select("SELECT * FROM matchinfo WHERE id = #{id}")
  MatchInfo selectMatchInfoById(int id);

  @Select("SELECT * FROM matchinfo")
  ArrayList<MatchInfo> selectAllMatchInfo();
}
