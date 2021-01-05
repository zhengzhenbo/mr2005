package com.baidu.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

/**
 * @ClassName CategoryBrandEntity
 * @Description: TODO
 * @Author shenyaqi
 * @Date 2020/12/28
 * @Version V1.0
 **/
@Data
@Table(name = "tb_category_brand")
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBrandEntity {

    private Integer categoryId;

    private Integer brandId;
}