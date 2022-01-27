package mybatis.mapper;

import mybatis.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    List<UserEntity> listAllUser();

    @Select(" select * from user_info where id =#{userid}")
    UserEntity getUserById(@Param("userid") String userId);
}
