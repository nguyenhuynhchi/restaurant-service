package com.option1.restaurant_service.enums;

import lombok.Getter;

@Getter
public enum StatusReservation {
    UNCONFIRMED("⏳ Chờ quản lý nhà hàng xác nhận bàn"),
    CONFIRMED("✅ Đã được quản lý nhà hàng xác nhận bàn"),
    GOT_TABLE("✨ Khách đã nhận bàn"),
    TABLE_CANCLE("❌ Đã hủy bàn"),
    TABLE_CANCLE_ADMIN("❌ Hiện tại đã hết bàn. Xin quý khách thông cảm !")
    ;
    StatusReservation(String description) {
        this.description = description;
    }

    private String description;

}
