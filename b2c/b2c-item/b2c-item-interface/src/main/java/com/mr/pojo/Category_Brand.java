package com.mr.pojo;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "tb_category_brand")
public class Category_Brand {
    private  Long categoryId;
    private  Long brandId;
}
