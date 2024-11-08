package fan.yangyi.javacloudstorage.untils;

import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class BRUtils {
    static public List<String> getBeautyErrors(List<FieldError> errors) {
        var beautyErrors = new ArrayList<String>();
        for (FieldError error : errors) {
            beautyErrors.add(error.getDefaultMessage());
        }
        return beautyErrors;
    }
}
