package oit.is.z2620.kaizi.janken.model;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
// import org.springframework.stereotype.Component;

@Mapper
public interface UserMapper {
  @Select("select * from users where id=#{id}")
  User selectById(int id);

  @Select("select * from users where name=#{name}")
  User selectByName(String Name);

  @Select("select * from users")
  ArrayList<User> selectAllUser();

  @Insert("insert into users (name) values (#{name})")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertUser(User user);
}
