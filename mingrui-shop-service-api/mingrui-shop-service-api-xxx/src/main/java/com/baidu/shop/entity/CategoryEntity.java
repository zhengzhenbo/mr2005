package com.baidu.shop.entity;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName CategoryEntity
 * @Description: TODO
 * @Author shenyaqi
 * @Date 2020/12/22
 * @Version V1.0
 **/
@ApiModel(value = "分类实体类")//swagger的注解 : 声明模型
@Data
@Table(name = "tb_category") //java 的实体类和数据库中的表做映射
public class CategoryEntity {

    @Id //声明主键
    @ApiModelProperty(value = "类目id",example = "1") //所有的数字类型在写swagger的时候需要加example="默认值",测试接口
    @NotNull(message = "id不能为空", groups = {MingruiOperation.Update.class})
    private Integer id;//如果没有@Column 默认使用entity的属性名和 数据库表的字段名做映射

    @ApiModelProperty(value = "类目名称")
    @NotEmpty(message = "分类名称不能为空", groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private String name;

    @ApiModelProperty(value = "父类目id,顶级类目填0",example = "1")
    @NotNull(message = "父级分类不能为null",groups = {MingruiOperation.Add.class})
    private Integer parentId;//会自动将驼峰转换为 _x的形式

    @ApiModelProperty(value = "是否是父级节点",example = "1")
    @NotNull(message = "是否为父级节点不能为null",groups = {MingruiOperation.Add.class})
    private Integer isParent;

    @ApiModelProperty(value = "排序",example = "1")
    @NotNull(message = "排序字段不能为null",groups = {MingruiOperation.Add.class})
    private Integer sort;
}