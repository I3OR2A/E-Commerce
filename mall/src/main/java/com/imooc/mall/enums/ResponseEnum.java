package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum ResponseEnum {
    ERROR(-1, "服務端錯誤"),

    SUCCESS(0, "成功"),

    PASSWORD_ERROR(1, "密碼錯誤"),

    USERNAME_EXIST(2, "用戶名稱已存在"),

    PARAM_ERROR(3,"參數錯誤"),

    EMAIL_EXIST(4, "郵箱已存在"),

    NEED_LOGIN(10, "用戶未登入,請先登入"),

    USERNAME_OR_PASSWORD_ERROR (11, "用戶名或密碼錯誤");

    Integer code;

    String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
