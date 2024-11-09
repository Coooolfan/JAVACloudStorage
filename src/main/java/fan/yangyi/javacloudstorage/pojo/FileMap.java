package fan.yangyi.javacloudstorage.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@TableName("filemap")
@NoArgsConstructor
public class FileMap {

    @TableId(type = IdType.AUTO)
    private Integer id;

//    单位KB
    private Integer size;

    private String filename;

    private String format;

    private Boolean isDirectory;

    private Integer parent;

    private Integer owner;

    private Boolean isRoot;

    private String filePath;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public FileMap(String filename, String format, Boolean isDirectory, Integer parent, Integer owner, Boolean isRoot, Integer size) {
        this.filename = filename;
        this.format = format;
        this.isDirectory = isDirectory;
        this.parent = parent;
        this.owner = owner;
        this.isRoot = isRoot;
        this.filePath = generateFilePath();
        this.size = size;
    }

    public static String generateFilePath() {
        LocalDate date = LocalDate.now();
        String year = String.valueOf(date.getYear());
        String month = String.format("%02d", date.getMonthValue());
        String uuid = UUID.randomUUID().toString();

        return year + "/" + month + "/" + uuid;
    }
}
