package fan.yangyi.javacloudstorage.exception;

public class UnderQuotaException extends RuntimeException {
    public UnderQuotaException(String message) {
        super(message);
    }
}
