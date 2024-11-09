package fan.yangyi.javacloudstorage.dto;

import fan.yangyi.javacloudstorage.pojo.FileMap;
import lombok.Data;

import java.util.List;

@Data
public class FilemapGetResp {
    private FileMap current;
    private List<FileMap> children;
}
