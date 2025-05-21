package com.option1.restaurant_service.enums;

import lombok.Getter;

@Getter
public enum StatusReservation {
    UNCONFIRMED("❎ Chưa được quản lý nhà hàng xác nhận bàn"),
    CONFIRMED("✅ Đã được quản lý nhà hàng xác nhận bàn"),
    GOT_TABLE("✨ Khách đã nhận bàn"),
    TABLE_CANCLE("❌ Khách đã hủy bàn"),
    TABLE_CANCLE_ADMIN("❌ Quản lý nhà hàng đã hủy bàn")
    ;
    StatusReservation(String description) {
        this.description = description;
    }

    private String description;

}
