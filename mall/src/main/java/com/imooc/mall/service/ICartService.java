package com.imooc.mall.service;

import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.vo.ResponseVo;

public interface ICartService {

    ResponseVo add(Integer uid, CartAddForm cartAddForm);

    ResponseVo list(Integer uid);

    ResponseVo update(Integer uid, Integer productId, CartUpdateForm form);

    ResponseVo delete(Integer uid, Integer productId);

    ResponseVo selectAll(Integer uid);

    ResponseVo unSelectAll(Integer uid);

    ResponseVo sum(Integer uid);
}
