package exceptions;

public class ParcelDeliveryException extends RuntimeException {
    public ParcelDeliveryException(String message) {
        super(message);
    }
}
