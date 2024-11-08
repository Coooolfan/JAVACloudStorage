package fan.yangyi.javacloudstorage.controller;

import fan.yangyi.javacloudstorage.pojo.User;
import fan.yangyi.javacloudstorage.service.UserService;
import fan.yangyi.javacloudstorage.untils.BRUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/token")
    public String login(@ModelAttribute User user, BindingResult bindingResult, Model model) {
        // 验证字段
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "index";
        }

        // 执行登录操作
        User userLogined = userService.login(user);
        if (userLogined != null) {
            return "dashboard";
        }

        // 登录失败
        model.addAttribute("loginError", "用户名或密码错误");
        return "index";
    }


    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult BR) {
        if (BR.hasErrors()) {
            return new ResponseEntity<>(BRUtils.getBeautyErrors(BR.getFieldErrors()), HttpStatus.BAD_REQUEST);
        }
        User userRegistered = userService.register(user);
        if (userRegistered != null) {
            return new ResponseEntity<>(userRegistered, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
