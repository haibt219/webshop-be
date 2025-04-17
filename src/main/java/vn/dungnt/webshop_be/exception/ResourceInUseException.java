package vn.dungnt.webshop_be.exception;

public class ResourceInUseException extends RuntimeException {
  public ResourceInUseException(String message) {
    super(message);
  }
}
