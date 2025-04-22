package vn.dungnt.webshop_be.entity;

public enum RoleEnum {
  ADMIN("ADMIN"),
  SALESMAN("SALESMAN"),
  CUSTOMER("CUSTOMER");

  private final String value;

  RoleEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
