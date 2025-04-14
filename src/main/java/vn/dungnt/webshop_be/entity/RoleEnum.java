package vn.dungnt.webshop_be.entity;

public enum RoleEnum {
    ADMIN("ROLE_ADMIN"),
    SALESMAN("ROLE_SALESMAN"),
    CUSTOMER("ROLE_CUSTOMER");

    private final String value;

    RoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
