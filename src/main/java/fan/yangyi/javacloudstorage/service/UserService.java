package fan.yangyi.javacloudstorage.service;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fan.yangyi.javacloudstorage.pojo.User;
import fan.yangyi.javacloudstorage.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final String SALT = "coooolfan.com";

    private final UserMapper userMapper;
    private final FileMapService fileMapService;

    @Autowired
    public UserService(UserMapper userMapper, FileMapService fileMapService) {
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
        User user = new User(null, username, SaSecureUtil.sha512(password + SALT));
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
        if (user != null && user.getPassword().equals(SaSecureUtil.sha512(password + SALT))) {
            StpUtil.login(user.getId());
            return user;
        }
        return null;
    }

    public User login(User user) {
        return login(user.getUsername(), user.getPassword());
    }
}