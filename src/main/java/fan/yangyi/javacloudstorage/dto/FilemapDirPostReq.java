package fan.yangyi.javacloudstorage.dto;

import lombok.Data;

@Data
public class FilemapDirPostReq {
    private String dirName;
    private Long parentId;
}
