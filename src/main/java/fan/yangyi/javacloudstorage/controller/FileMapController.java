package fan.yangyi.javacloudstorage.controller;


import cn.dev33.satoken.stp.StpUtil;
import fan.yangyi.javacloudstorage.pojo.FileMap;
import fan.yangyi.javacloudstorage.pojo.User;
import fan.yangyi.javacloudstorage.service.FileMapService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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


    @GetMapping("/root")
    public ResponseEntity<FileMap> getRootDir(){
//        此方法自带权限验证
        val loginId = StpUtil.getLoginIdAsLong();
        return ResponseEntity.ok(fileMapService.getRootDir(loginId));
    }

    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }
}
