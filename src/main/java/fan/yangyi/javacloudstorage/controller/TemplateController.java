package fan.yangyi.javacloudstorage.controller;

import fan.yangyi.javacloudstorage.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {

    @GetMapping("/login")
    public String login(Model model) {
        // 初始化User对象并传递到模板
        model.addAttribute("user", new User());
        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

}