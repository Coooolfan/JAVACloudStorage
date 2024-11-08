package fan.yangyi.javacloudstorage.controller;


import cn.dev33.satoken.stp.StpUtil;
import fan.yangyi.javacloudstorage.service.FileMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/filemap")
public class FileMapController {
    private final FileMapService fileMapService;

    @Autowired
    public FileMapController(FileMapService fileMapService) {
        this.fileMapService = fileMapService;
    }


    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }
}
