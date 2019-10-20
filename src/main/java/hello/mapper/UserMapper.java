package hello.mapper;

import hello.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {
    @Select("SELECT * FROM USER WHERE username = #{username}")
    User findUserByUserName(@Param("username") String username);

    @Select("INSERT INTO USER(username,encoded_password,created_at,updated_at)VALUES(#{username},#{encodedPassword},now(),now())")
    void storeIntoDatabase(@Param("username") String username, @Param("encodedPassword") String password);
}
