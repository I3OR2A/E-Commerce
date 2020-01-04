package com.imooc.mall.form;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UserRegisterForm {

    @NotBlank
    // @NotNull
    // @NotEmpty
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;
}
