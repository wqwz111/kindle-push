package personal.leo.kindlepush.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import personal.leo.kindlepush.model.ErrorResult;

import javax.mail.MessagingException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ErrorResult handleBadRequestException(BadRequestException e) {
        return new ErrorResult(10001, e.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    public ErrorResult handleUserException(AuthException e) {
        return new ErrorResult(10000, e.getMessage());
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<?> handleStorageException(StorageException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<?> handleMessagingException(MessagingException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResult handleAccessDeniedException(AccessDeniedException e) {
        return new ErrorResult(10002, "Current user has no permission to access target resource.");
    }
}
