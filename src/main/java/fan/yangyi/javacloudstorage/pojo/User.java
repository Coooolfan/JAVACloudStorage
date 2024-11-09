package fan.yangyi.javacloudstorage.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @Length(min = 6, message = "用户名长度不得小于6")
    private String username;
    //  长度至少6个字符，至少包含一个特殊字符和数字
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{6,}$",
            message = "密码长度至少6个字符，至少包含一个特殊字符和数字"
    )
    private String password;

    private String avatar;

    private Long quota;

    private Long usedQuota;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.avatar = null;
        this.quota = 1024*1024L; // 默认 1GB
        this.createTime = null;
        this.updateTime = null;
        this.usedQuota = 0L;
    }


}
