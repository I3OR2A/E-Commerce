package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum ResponseEnum {
    ERROR(-1, "服務端錯誤"),

    SUCCESS(0, "成功"),

    PASSWORD_ERROR(1, "密碼錯誤"),

    USERNAME_EXIST(2, "用戶名稱已存在"),

    PARAM_ERROR(3, "參數錯誤"),

    EMAIL_EXIST(4, "郵箱已存在"),

    NEED_LOGIN(10, "用戶未登入,請先登入"),

    USERNAME_OR_PASSWORD_ERROR(11, "用戶名或密碼錯誤"),

    PRODUCT_OFF_OR_DELETE(12, "商品下架或刪除"),

    PRODUCT_NOT_EXIST(13, "商品不存在"),

    PRODUCT_STOCK_ERROR(14, "庫存不正確"),

    CART_PRODUCT_NOT_EXIST(15, "購物車裡無此商品"),
    ;

    Integer code;

    String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
