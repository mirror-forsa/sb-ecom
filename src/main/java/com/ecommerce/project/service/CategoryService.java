package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;

import java.util.List;

public interface CategoryService {
    // 接口实现get
    List<Category> getAllCategories();
    // 创建类别
    void createCategory(Category category);
    // 删除类别
    String deleteCategory(Long categoryId);

    // 更新类别
    Category updateCategory(Category category, Long categoryId);
}
