package com.ecommerce.project.repositories;


import com.ecommerce.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

// JapRepository <表名， 主键的字符类型>
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
