package com.imooc.mall.pojo;

import lombok.Data;

import java.sql.Date;

/**
 * po (persistent object)
 * pojo (plain ordinay java object)
 */
@Data
public class Category {

    private Integer id;
    private Integer parentId;
    private String name;
    private Integer status;
    private Integer sortOrder;
    private Date createTime;
    private Date updateTime;
}
