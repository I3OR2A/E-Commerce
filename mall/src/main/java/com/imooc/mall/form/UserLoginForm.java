package com.imooc.mall.form;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UserLoginForm {

    @NotBlank // 用於 String 判斷空格
    // @NotNull
    // @NotEmpty 用於集合
    private String username;

    @NotBlank
    private String password;
}
