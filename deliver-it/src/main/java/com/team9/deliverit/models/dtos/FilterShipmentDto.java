package com.team9.deliverit.models.dtos;

public class FilterShipmentDto {

    private Integer warehouseId;

    private Integer userId;

    public FilterShipmentDto() {
    }

    public FilterShipmentDto(Integer warehouseId, Integer userId) {
        this.warehouseId = warehouseId;
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }
}
