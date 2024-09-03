package me.zhengjie.modules.tc.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.tc.domain.Category;
import me.zhengjie.modules.tc.repository.CategoryRepository;
import me.zhengjie.modules.tc.service.CategoryService;
import me.zhengjie.modules.tc.service.dto.CategoryDTO;
import me.zhengjie.modules.tc.service.dto.CategoryQueryCriteria;
import me.zhengjie.modules.tc.service.mapstruct.CategoryMapper;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    @Override
    public PageResult<CategoryDTO> queryAll(CategoryQueryCriteria criteria, Pageable pageable) {
        Page<Category> page = categoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return  PageUtil.toPage(page.map(categoryMapper::toDto));
    }

    @Override
    public List<CategoryDTO> queryAll(CategoryQueryCriteria criteria) {
        List<Category> list = categoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        List<CategoryDTO> dto = categoryMapper.toDto(list);
        return deduplication(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Category resources) {
        categoryRepository.findByName(resources.getName()).ifPresent(i -> {
            throw new RuntimeException("分类名称已存在");
        });
        if(resources.getPid()==0){
            resources.setPid(null);
        }
        categoryRepository.save(resources);
        // 计算子节点数目
        resources.setSubCount(0);
        // 更新父节点菜单数目
        updateSubCnt(resources.getPid());
    }
    private void updateSubCnt(Long menuId){
        if(menuId != null){
            int count = categoryRepository.countByPid(menuId);
            categoryRepository.updateSubCntById(count, menuId);
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(Category resources) {
        categoryRepository.findByName(resources.getName()).ifPresent(i -> {
            if(!i.getId().equals(resources.getId())){
                throw new RuntimeException("分类名称已存在");
            }
        });
        if(resources.getPid()==0){
            resources.setPid(null);
        }
        categoryRepository.saveAndFlush(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            List<Category> menuList = categoryRepository.findByPid(id);
            menuList.add(categoryRepository.getById(id));
            categoryRepository.deleteAll(menuList);
        }
    }

    @Override
    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id).orElseGet(Category::new);
        ValidationUtil.isNull(category.getId(),"Category","id",id);
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryDTO> getSuperior(CategoryDTO categoryDto,List<Category> categories) {
        if(categoryDto.getPid() == null){
            categories.addAll(categoryRepository.findByPidIsNullOrderBySort());
            return categoryMapper.toDto(categories);
        }
        categories.addAll(categoryRepository.findByPidOrderBySort(categoryDto.getPid()));
        return getSuperior(findById(categoryDto.getPid()), categories);
    }

    @Override
    public List<CategoryDTO> buildTree(ArrayList<CategoryDTO> categoryDTOS) {
        List<CategoryDTO> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (CategoryDTO menuDTO : categoryDTOS) {
            if (menuDTO.getPid() == null) {
                trees.add(menuDTO);
            }
            for (CategoryDTO it : categoryDTOS) {
                if (menuDTO.getId().equals(it.getPid())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<>());
                    }
                    menuDTO.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        if(trees.isEmpty()){
            trees = categoryDTOS.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        return trees;
    }

    @Override
    public List<CategoryDTO> getMenus(Long pid) {
        List<Category> menus;
        if(pid != null && !pid.equals(0L)){
            menus = categoryRepository.findByPidOrderBySort(pid);
        } else {
            menus = categoryRepository.findByPidIsNullOrderBySort();
        }
        return categoryMapper.toDto(menus);
    }


    private List<CategoryDTO> deduplication(List<CategoryDTO> list) {
        List<CategoryDTO> deptDtos = new ArrayList<>();
        for (CategoryDTO deptDto : list) {
            boolean flag = true;
            for (CategoryDTO dto : list) {
                if (dto.getId().equals(deptDto.getPid())) {
                    flag = false;
                    break;
                }
            }
            if (flag){
                deptDtos.add(deptDto);
            }
        }
        return deptDtos;
    }
}
