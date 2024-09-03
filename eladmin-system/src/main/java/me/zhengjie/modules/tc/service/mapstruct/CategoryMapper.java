package me.zhengjie.modules.tc.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.modules.tc.domain.Category;
import me.zhengjie.modules.tc.service.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper  extends BaseMapper<CategoryDTO, Category> {
}
