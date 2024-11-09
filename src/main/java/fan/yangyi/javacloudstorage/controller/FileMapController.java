package fan.yangyi.javacloudstorage.controller;


import cn.dev33.satoken.stp.StpUtil;
import fan.yangyi.javacloudstorage.dto.FilemapDirPostReq;
import fan.yangyi.javacloudstorage.dto.FilemapGetResp;
import fan.yangyi.javacloudstorage.dto.FilemapPatchReq;
import fan.yangyi.javacloudstorage.pojo.FileMap;
import fan.yangyi.javacloudstorage.service.FileMapService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/filemap")
public class FileMapController {
    private final FileMapService fileMapService;

    @Autowired
    public FileMapController(FileMapService fileMapService) {
        this.fileMapService = fileMapService;
    }


    //    获取当前用户的根目录ID
    @GetMapping("/root/id")
    public ResponseEntity<FileMap> getRootDir() {
        // 此方法自带权限验证
        val loginId = StpUtil.getLoginIdAsLong();
        return ResponseEntity.ok(fileMapService.getRootDir(loginId));
    }


    //    获取当前用户的根目录下的所有文件
    @GetMapping("/root")
    public ResponseEntity<List<FileMap>> getRootDirFiles() {
        val loginId = StpUtil.getLoginIdAsLong();
        return ResponseEntity.ok(fileMapService.getRootDirFiles(loginId));
    }

    //    获取指定目录下的所有文件
    @GetMapping("/{id}")
    public ResponseEntity<FilemapGetResp> getDirById(@PathVariable Long id) {
        val loginId = StpUtil.getLoginIdAsLong();
        FilemapGetResp resp = new FilemapGetResp();
        resp.setCurrent(fileMapService.getFileDetail(loginId, id));
        resp.setChildren(fileMapService.getDirChildren(loginId, id));
        return ResponseEntity.ok(resp);
    }

    //    创建目录
    @PostMapping("/dir")
    public ResponseEntity<FileMap> createDir(@RequestBody FilemapDirPostReq request) {
        val loginId = StpUtil.getLoginIdAsLong();
        return ResponseEntity.ok(fileMapService.createDir(loginId, request.getDirName(), request.getParentId()));
    }

    //    上传、秒传
    @GetMapping("/file")
    public ResponseEntity<FileMap> createFileBySHA(@RequestParam("filename") String filename,
                                                   @RequestParam("format") String format,
                                                   @RequestParam("parentId") Long  parentId,
                                                   @RequestParam("sha256") String sha256
    ) {
        val loginId = StpUtil.getLoginIdAsLong();
        FileMap fileBySHA = fileMapService.createFileBySHA(loginId, filename, format, parentId, sha256);
        if (fileBySHA == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else
            return ResponseEntity.ok(fileBySHA);
    }

    //    上传
    @PostMapping("/file")
    public ResponseEntity<FileMap> createFile(@RequestParam("filename") String filename,
                                              @RequestParam("format") String format,
                                              @RequestParam("parentId") Long parent,
                                              @RequestParam("file") MultipartFile file,
                                              @RequestParam("sha256") String SHA256
    ) {
        val loginId = StpUtil.getLoginIdAsLong();
        return ResponseEntity.ok(fileMapService.createFile(loginId, filename, format, parent, file, SHA256));
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        // 调用Service层获取文件
        val getResp = fileMapService.getFile(id);

        // 设置下载的HTTP头信息
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + getResp.getFilename());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .body(getResp.getFile());
    }

    @DeleteMapping("/file/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
        val loginId = StpUtil.getLoginIdAsLong();
        if (fileMapService.deleteFile(loginId, id))
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/file/{id}")
    public ResponseEntity<FileMap> renameFile(@PathVariable Long id, @RequestBody FilemapPatchReq req) {
        val loginId = StpUtil.getLoginIdAsLong();
        return ResponseEntity.ok(fileMapService.renameFile(loginId, id, req.getFilename()));
    }
}
