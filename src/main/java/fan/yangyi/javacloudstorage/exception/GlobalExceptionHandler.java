package fan.yangyi.javacloudstorage.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
// 未登录异常，返回401
    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<String> handleUnauthorizedException(NotLoginException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

// 权限异常，返回401
    @ExceptionHandler(NotPermissionException.class)
    public ResponseEntity<String> handleNotPermissionException(NotPermissionException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

// 自定义异常，用户空间不足，返回400
    @ExceptionHandler(UnderQuotaException.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}