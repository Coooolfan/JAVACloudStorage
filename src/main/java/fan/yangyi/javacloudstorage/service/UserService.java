package fan.yangyi.javacloudstorage.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fan.yangyi.javacloudstorage.pojo.User;
import fan.yangyi.javacloudstorage.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final FIleMapService fileMapService;

    @Autowired
    public UserService(UserMapper userMapper, FIleMapService fileMapService) {
        this.userMapper = userMapper;
        this.fileMapService = fileMapService;
    }


    public User register(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        User existUser = userMapper.selectOne(queryWrapper);
        if (existUser != null) {
            return null;
        }
        User user = new User(null, username, password);
        userMapper.insert(user);
        // 为这个用户创建一个Root文件夹
        fileMapService.createRootDir(user);
        return user;
    }

    public User register(User user) {
        return register(user.getUsername(), user.getPassword());
    }

    public User login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User login(User user) {
        return login(user.getUsername(), user.getPassword());
    }
}