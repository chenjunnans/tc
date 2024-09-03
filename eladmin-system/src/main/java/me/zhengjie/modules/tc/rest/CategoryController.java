package me.zhengjie.modules.tc.rest;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.service.dto.MenuDto;
import me.zhengjie.modules.tc.domain.Category;
import me.zhengjie.modules.tc.service.CategoryService;
import me.zhengjie.modules.tc.service.dto.CategoryDTO;
import me.zhengjie.modules.tc.service.dto.CategoryQueryCriteria;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
@Api(tags = "分类管理")
public class CategoryController {


    private  final CategoryService categoryService;
    @ApiOperation("查询分类")
    @GetMapping
    @PreAuthorize("@el.check('category:list')")
    public ResponseEntity<PageResult<CategoryDTO>> queryDept(CategoryQueryCriteria criteria, Pageable pageable) {
        if(criteria.getPid()==null||criteria.getPid()==0){
            criteria.setPidIsNull(true);
        }
        return new ResponseEntity<>( categoryService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @ApiOperation("查询分类--小程序")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<PageResult<CategoryDTO>> queryDept(CategoryQueryCriteria criteria) {
        List<CategoryDTO> data = categoryService.queryAll(criteria);
        buildTree(data,criteria);
//        data.forEach(categoryDTO -> {
//            criteria.setPid(categoryDTO.getId());
//            List<CategoryDTO> categoryDTOS = categoryService.queryAll(criteria);
//            categoryDTO.setChildren(categoryDTOS);
//            categoryDTO.setSubmenu(categoryDTOS);
//            categoryDTO.setValue(categoryDTO.getId());
//            categoryDTO.setSubmenu(categoryDTOS);
//            categoryDTO.setValue(categoryDTO.getId());
//
//        });
        return new ResponseEntity<>( PageUtil.toPage(data,data.size()), HttpStatus.OK);
    }
    public void  buildTree(List<CategoryDTO> categories,CategoryQueryCriteria criteria) {
        categories.forEach(categoryDTO -> {
            criteria.setPid(categoryDTO.getId());
            List<CategoryDTO> categoryDTOS = categoryService.queryAll(criteria);
            if(!categoryDTOS.isEmpty()){
                categoryDTO.setChildren(categoryDTOS);
                buildTree(categoryDTOS,criteria);
            }
        });
    }


    @ApiOperation("查询分类--小程序")
    @PostMapping("submenu")
    @AnonymousAccess
    public ResponseEntity<PageResult<CategoryDTO>> submenu(CategoryQueryCriteria criteria) {
        List<CategoryDTO> data = categoryService.queryAll(criteria);
        CategoryDTO categoryDTO1 = new CategoryDTO();
        data.forEach(categoryDTO -> {
            criteria.setPid(categoryDTO.getId());
            List<CategoryDTO> categoryDTOS = categoryService.queryAll(criteria);
            categoryDTO.setChildren(categoryDTOS);

        });
        return new ResponseEntity<>( PageUtil.toPage(data,data.size()), HttpStatus.OK);
    }

    @Log("新增分类")
    @ApiOperation("新增分类")
    @PostMapping("add")
    @PreAuthorize("@el.check('category:add')")
    public ResponseEntity<Object> createDept(@Validated @RequestBody Category resources){
        if (resources.getId() != null) {
            throw new BadRequestException("新增部门失败！");
        }
        categoryService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Log("修改分类")
    @ApiOperation("修改分类")
    @PostMapping("modify")
    @PreAuthorize("@el.check('category:modify')")
    public ResponseEntity<Object> modify(@Validated @RequestBody Category resources){
        categoryService.modify(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Log("删除分类")
    @ApiOperation("删除分类")
    @PostMapping("delete")
    @PreAuthorize("@el.check('category:delete')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
        categoryService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @ApiOperation("查询分类:根据ID获取同级与上级数据")
    @PostMapping("/superior")
    @AnonymousAccess
    public ResponseEntity<List<CategoryDTO>> getMenuSuperior(@RequestBody List<Long> ids) {
        Set<CategoryDTO> categoryDtos = new LinkedHashSet<>();
        if(CollectionUtil.isNotEmpty(ids)){
            for (Long id : ids) {
                CategoryDTO categoryDto = categoryService.findById(id);
                List<CategoryDTO> menuDtoList = categoryService.getSuperior(categoryDto, new ArrayList<>());
                for (CategoryDTO categoryDTO : menuDtoList) {
                    if(categoryDTO.getId().equals(categoryDto.getPid())) {
                        categoryDTO.setSubCount(categoryDTO.getSubCount() - 1);
                    }
                }
                categoryDtos.addAll(menuDtoList);
            }
            // 编辑菜单时不显示自己以及自己下级的数据，避免出现PID数据环形问题
//            categoryDtos = categoryDtos.stream().filter(i -> !ids.contains(i.getId())).collect(Collectors.toSet());
            return new ResponseEntity<>(categoryService.buildTree(new ArrayList<>(categoryDtos)),HttpStatus.OK);
        }
        return new ResponseEntity<>(categoryService.getMenus(null),HttpStatus.OK);
    }
    @ApiOperation("查询分类:根据ID获取同级与上级数据")
    @PostMapping("/superior2")
    @AnonymousAccess
    public ResponseEntity<List<CategoryDTO>> getMenuSuperior2(@RequestBody Long id) {
        List<CategoryDTO> categories = new ArrayList<>();
        CategoryDTO byId = categoryService.findById(id);
        categories.add(byId);
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }
    @ApiOperation("返回全部的分类")
    @GetMapping(value = "/lazy")
    @AnonymousAccess
    public ResponseEntity<List<CategoryDTO>> queryAllMenu(@RequestParam Long pid){
        return new ResponseEntity<>(categoryService.getMenus(pid),HttpStatus.OK);
    }
}
