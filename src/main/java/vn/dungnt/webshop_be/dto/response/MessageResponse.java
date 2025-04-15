package vn.dungnt.webshop_be.dto.response;

public class MessageResponse {
  private String message;

  MessageResponse() {}

  public MessageResponse(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
