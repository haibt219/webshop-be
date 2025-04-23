package vn.dungnt.webshop_be.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StorageType {
  SSD("SSD"),
  HDD("HDD"),
  HYBRID("HYBRID"),
  EMMC("EMMC");

  private final String value;

  StorageType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static StorageType fromValue(String value) {
    for (StorageType type : StorageType.values()) {
      if (type.value.equalsIgnoreCase(value)) {
        return type;
      }
    }
    throw new IllegalArgumentException(
        "Không hợp lệ StorageType: " + value + ". Các giá trị hợp lệ là: SSD, HDD, HYBRID, EMMC");
  }
}
