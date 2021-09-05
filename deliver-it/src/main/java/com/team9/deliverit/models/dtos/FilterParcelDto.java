package com.team9.deliverit.models.dtos;

public class FilterParcelDto {

    private String status;

    private String category;

    private String weight;

    private Integer warehouseId;

    private Integer userId;

    public FilterParcelDto() {
    }

    public FilterParcelDto(String status, String category, String weight, Integer warehouseId, Integer userId) {
        this.status = status;
        this.category = category;
        this.weight = weight;
        this.warehouseId = warehouseId;
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
