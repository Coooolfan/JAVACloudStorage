package fan.yangyi.javacloudstorage.service;

import fan.yangyi.javacloudstorage.dto.FilemapFileDownloadResp;
import fan.yangyi.javacloudstorage.exception.UnderQuotaException;
import fan.yangyi.javacloudstorage.mapper.UserMapper;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fan.yangyi.javacloudstorage.mapper.FileMapMapper;
import fan.yangyi.javacloudstorage.pojo.FileMap;
import org.springframework.core.io.FileSystemResource;
import fan.yangyi.javacloudstorage.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileMapService {

    private static final Logger log = LoggerFactory.getLogger(FileMapService.class);
    private final FileMapMapper fileMapMapper;
    private final UserMapper userMapper;

    @Value("${file.storage.path}")
    private String storagePath;

    @Autowired
    public FileMapService(FileMapMapper fileMapMapper, UserMapper userMapper) {
        this.fileMapMapper = fileMapMapper;
        this.userMapper = userMapper;
    }

    public FileMap createRootDir(User user) {
        FileMap rootDir = new FileMap("", "", true, 0, user.getId(), true, 0);
        fileMapMapper.insert(rootDir);
        return rootDir;
    }

    public FileMap getRootDir(long loginIdAsLong) {
        QueryWrapper<FileMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FileMap::getOwner, loginIdAsLong).eq(FileMap::getIsRoot, true);
        return fileMapMapper.selectOne(queryWrapper);
    }

    public List<FileMap> getDirChildren(long userId, long fileId) {
        if (fileId == -1) {
            FileMap rootDir = getRootDir(userId);
            fileId = rootDir.getId();
        }
        QueryWrapper<FileMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FileMap::getOwner, userId).eq(FileMap::getParent, fileId);
        return fileMapMapper.selectList(queryWrapper);
    }

    public FileMap getFileDetail(long loginId, long fileId) {
        QueryWrapper<FileMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FileMap::getId, fileId).eq(FileMap::getOwner, loginId);
        return fileMapMapper.selectOne(queryWrapper);
    }

    public List<FileMap> getRootDirFiles(long loginId) {
        QueryWrapper<FileMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FileMap::getOwner, loginId).eq(FileMap::getIsRoot, true);
        FileMap rootDir = fileMapMapper.selectOne(queryWrapper);
        return getDirChildren(loginId, rootDir.getId());
    }

    public FileMap createDir(Long loginId, String dirName, Long parentId) {
        if (parentId == -1) {
//            获取根目录ID
            FileMap rootDir = getRootDir(loginId);
            parentId = Long.valueOf(rootDir.getId());
        }


        QueryWrapper<FileMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FileMap::getOwner, loginId)
                .eq(FileMap::getParent, parentId)
                .eq(FileMap::getFilename, dirName);
        if (fileMapMapper.selectOne(queryWrapper) != null) {
            return null;
        }
        FileMap fileMap = new FileMap(dirName, "", true, parentId.intValue(), loginId.intValue(), false, 0);
        fileMapMapper.insert(fileMap);
        return fileMap;
    }

    public FileMap createFile(Long loginId, String filename, String format, Long parent, MultipartFile file) {
        if (parent == -1) {
//            获取根目录ID
            FileMap rootDir = getRootDir(loginId);
            parent = Long.valueOf(rootDir.getId());
        }
        Long size = file.getSize() / 1024;
        val user = userMapper.selectById(loginId);
        if(user.getQuota()-user.getUsedQuota()-size<0){
            throw new UnderQuotaException("用户剩余空间不足");
        }
        user.setUsedQuota(user.getUsedQuota()+size);
        QueryWrapper<FileMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(FileMap::getOwner, loginId)
                .eq(FileMap::getParent, parent)
                .eq(FileMap::getFilename, filename)
                .eq(FileMap::getFormat, format);
        if (fileMapMapper.selectOne(queryWrapper) != null) {
            return null;
        }

        FileMap fileMap = new FileMap(filename, format, false, parent.intValue(), loginId.intValue(), false, size.intValue());
        fileMapMapper.insert(fileMap);
        userMapper.updateById(user);
        val saveFiles = saveFiles(file, fileMap.getFilePath());
        log.info("文件保存结果：" + saveFiles);
        return fileMap;
    }

    // 写入硬盘
    private Boolean saveFiles(MultipartFile file, String path) {

        path = storagePath + path;
        log.info("文件路径：" + path);

        // 创建目标文件对象
        File destinationFile = new File(path);

        // 检查目标路径是否存在，如果不存在则创建
        if (!destinationFile.getParentFile().exists()) {
            destinationFile.getParentFile().mkdirs();  // 创建目录
        }
        try {
            // 将上传的文件写入目标文件
            file.transferTo(destinationFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public FilemapFileDownloadResp getFile(Long id) {
        QueryWrapper<FileMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FileMap::getId, id);
        FileMap fileMap = fileMapMapper.selectOne(queryWrapper);
        if (fileMap == null) {
            return null;
        }
        // 根据文件名构建文件路径
        Path filePath = Paths.get(storagePath + fileMap.getFilePath());
        File file = filePath.toFile();
        FilemapFileDownloadResp filemapFileGetResp = new FilemapFileDownloadResp();
        filemapFileGetResp.setFile(new FileSystemResource(file));

        if (fileMap.getFormat().isBlank()) {
            filemapFileGetResp.setFilename(fileMap.getFilename());
        } else {
            filemapFileGetResp.setFilename(fileMap.getFilename() + "." + fileMap.getFormat());
        }
        // 使用FileSystemResource返回文件资源
        return filemapFileGetResp;
    }

    public Boolean deleteFile(long loginId, Long id) {
        QueryWrapper<FileMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FileMap::getId, id).eq(FileMap::getOwner, loginId);
        FileMap fileMap = fileMapMapper.selectOne(queryWrapper);
        if (fileMap == null) {
            return false;
        }
        fileMapMapper.deleteById(id);
        return true;
    }

    public FileMap renameFile(long loginId, Long id, String newFilename) {
        QueryWrapper<FileMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FileMap::getId, id).eq(FileMap::getOwner, loginId);
        FileMap fileMap = fileMapMapper.selectOne(queryWrapper);
        if (fileMap == null) {
            return null;
        }
        fileMap.setFilename(newFilename);
        fileMapMapper.updateById(fileMap);
        return fileMap;
    }
}
