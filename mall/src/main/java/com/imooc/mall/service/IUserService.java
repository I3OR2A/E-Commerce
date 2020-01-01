package com.imooc.mall.service;

import com.imooc.mall.pojo.User;
import org.springframework.stereotype.Service;

public interface IUserService {
    /**
     * 註冊
     */
    void register(User user);

    /**
     * 登入
     */
}
