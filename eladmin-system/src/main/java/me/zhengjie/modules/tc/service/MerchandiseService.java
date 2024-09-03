package me.zhengjie.modules.tc.service;

import me.zhengjie.modules.tc.domain.Category;
import me.zhengjie.modules.tc.domain.Merchandise;
import me.zhengjie.modules.tc.service.dto.CategoryDTO;
import me.zhengjie.modules.tc.service.dto.MerchandiseQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface MerchandiseService {
    PageResult<Merchandise> queryAll(MerchandiseQueryCriteria criteria, Pageable pageable);

    PageResult<Merchandise> queryAll2(MerchandiseQueryCriteria criteria, Pageable pageable);

    void create(Merchandise resources);

    void modify(Merchandise resources);

    void delete(Set<Long> ids);

    Merchandise query(Long id);

    void create2(Merchandise resources);

    void delete2(Long id);
}
