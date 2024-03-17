package xyz.trieye.assignment.deal.biz.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("select * from user_t where id=#{userId}")
    public UserVO queryUserById(int userId);
}
