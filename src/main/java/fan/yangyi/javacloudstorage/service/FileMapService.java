package fan.yangyi.javacloudstorage.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import fan.yangyi.javacloudstorage.mapper.FileMapMapper;
import fan.yangyi.javacloudstorage.pojo.FileMap;
import fan.yangyi.javacloudstorage.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileMapService {

    private final FileMapMapper fileMapMapper;

    @Autowired
    public FileMapService(FileMapMapper fileMapMapper) {
        this.fileMapMapper = fileMapMapper;
    }

    public FileMap createRootDir(User user) {
        FileMap rootDir = new FileMap("root", "", true, 0, user.getId(), true, 0);
        fileMapMapper.insert(rootDir);
        return rootDir;
    }

    public FileMap getRootDir(long loginIdAsLong) {
        QueryWrapper<FileMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FileMap::getOwner, loginIdAsLong).eq(FileMap::getIsRoot, 1);
        return fileMapMapper.selectOne(queryWrapper);
    }
}
