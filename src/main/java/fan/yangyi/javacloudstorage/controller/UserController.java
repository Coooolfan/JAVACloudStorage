package fan.yangyi.javacloudstorage.controller;

import fan.yangyi.javacloudstorage.mapper.UserMapper;
import fan.yangyi.javacloudstorage.pojo.User;
import fan.yangyi.javacloudstorage.service.UserService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/token")
    public String user(@RequestBody User user) {
        User userLogined = userService.login(user);
        if (userLogined != null) {
            return "token";
        }
        return "login failed";
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        User userRegistered = userService.register(user);
        if (userRegistered != null) {
            return userRegistered.toString();
        }
        return "register failed";
    }
}
