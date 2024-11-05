package fan.yangyi.javacloudstorage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fan.yangyi.javacloudstorage.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
