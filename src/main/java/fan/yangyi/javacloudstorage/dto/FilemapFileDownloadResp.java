package fan.yangyi.javacloudstorage.dto;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class FilemapFileDownloadResp {
    Resource file;
    String filename;
}
