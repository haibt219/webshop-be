package vn.dungnt.webshop_be.dto;

public class CustomerStatusDto {
    private String status;

    public CustomerStatusDto() {
    }

    public CustomerStatusDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
