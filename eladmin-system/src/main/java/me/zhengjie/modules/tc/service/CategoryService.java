package me.zhengjie.modules.tc.service;

import me.zhengjie.modules.tc.domain.Category;
import me.zhengjie.modules.tc.service.dto.CategoryDTO;
import me.zhengjie.modules.tc.service.dto.CategoryQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface CategoryService {

    PageResult<CategoryDTO> queryAll(CategoryQueryCriteria criteria, Pageable pageable);

    List<CategoryDTO> queryAll(CategoryQueryCriteria criteria);

    void create(Category resources);

    void modify(Category resources);

    void delete(Set<Long> ids);

    CategoryDTO findById(Long id);

    List<CategoryDTO> getSuperior(CategoryDTO categoryDto, List<Category> categories);

    List<CategoryDTO> buildTree(ArrayList<CategoryDTO> categoryDTOS);

    List<CategoryDTO> getMenus(Long pid);
}
