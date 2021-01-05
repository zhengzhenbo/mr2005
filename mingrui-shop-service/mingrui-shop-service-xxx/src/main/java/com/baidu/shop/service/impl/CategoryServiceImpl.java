package com.baidu.shop.service.impl;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryBrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.mapper.CategoryBrandMapper;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.service.CategoryService;
import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utils.ObjectUtil;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;


import java.util.List;

/**
 * @ClassName CategoryServiceImpl
 * @Description: TODO
 * @Author shenyaqi
 * @Date 2020/8/17
 * @Version V1.0
 **/
@RestController
public class CategoryServiceImpl extends BaseApiService implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;
    @Override
    public Result<List<CategoryEntity>> getCategoryByPid(Integer pid) {

        CategoryEntity categoryEntity = new CategoryEntity();

        categoryEntity.setParentId(pid);

        List<CategoryEntity> list = categoryMapper.select(categoryEntity);

        return this.setResultSuccess(list);
    }

    @Override
    public Result<List<CategoryEntity>> getCategoryByBrandId(Integer brandId) {

        List<CategoryEntity> list = categoryMapper.getCategoryByBrandId(brandId);

        return this.setResultSuccess(list);
    }


    @Transactional //以后增删改方法必须加这个注解
    @Override
    public Result<JsonObject> deleteCategoryByPid(Integer id) {

        if (ObjectUtil.isNull(id) || id <= 0) return this.setResultError(HTTPStatus.OPERATION_ERROR,"id不合法");

        CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(id);

        if (ObjectUtil.isNull(categoryEntity)) return this.setResultError(HTTPStatus.OPERATION_ERROR,"数据不存在");

        //判断当前节点是否为父节点(安全!)
        if (categoryEntity.getIsParent() == 1) return this.setResultError(HTTPStatus.OPERATION_ERROR,"当前节点为父错节点");//return之后的代码不会执行

        Example example1 = new Example(CategoryBrandEntity.class);
        example1.createCriteria().andEqualTo("categoryId",categoryEntity.getId());
        List<CategoryBrandEntity> categoryBrandEntities = categoryBrandMapper.selectByExample(example1);
        if (categoryBrandEntities.size()!=0) return this.setResultError(HTTPStatus.OPERATION_ERROR,"当前节点有关联的品牌，请先删除关联的品牌");

        //通过当前节点的父节点id 查询 当前节点(将要被删除的节点)的父节点下是否还有其他子节点
        Example example = new Example(CategoryEntity.class);
        example.createCriteria().andEqualTo("parentId", categoryEntity.getParentId());
        List<CategoryEntity> categoryList = categoryMapper.selectByExample(example);

        //如果size <= 1 --> 如果当前节点被删除的话 当前节点的父节点下没有节点了 --> 将当前节点的父节点状态改为叶子节点
        if (categoryList.size() <= 1) {

            CategoryEntity updateCategoryEntity = new CategoryEntity();
            updateCategoryEntity.setIsParent(0);
            updateCategoryEntity.setId(categoryEntity.getParentId());

            categoryMapper.updateByPrimaryKeySelective(updateCategoryEntity);
        }
        //通过id删除节点
        categoryMapper.deleteByPrimaryKey(id);

        return this.setResultSuccess();
    }


    @Transactional
    @Override
    public Result<JsonObject> editCategoryById(CategoryEntity categoryEntity) {
        categoryMapper.updateByPrimaryKeySelective(categoryEntity);
        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JsonObject> addCategoryById(CategoryEntity categoryEntity) {
        CategoryEntity parentCategoryEntity = new CategoryEntity();
        parentCategoryEntity.setId(categoryEntity.getParentId());
        parentCategoryEntity.setIsParent(1);
        categoryMapper.updateByPrimaryKeySelective(parentCategoryEntity);

        categoryMapper.insertSelective(categoryEntity);

        return this.setResultSuccess();
    }
}
