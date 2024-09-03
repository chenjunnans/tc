package me.zhengjie.modules.tc.service;

import me.zhengjie.modules.tc.domain.Merchant;
import me.zhengjie.modules.tc.service.dto.MerchandiseQueryCriteria;
import me.zhengjie.modules.tc.service.dto.Region2Dto;
import me.zhengjie.modules.tc.service.dto.RegionDto;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface MerchantService {
    PageResult<Merchant> queryAll(MerchandiseQueryCriteria criteria, Pageable pageable);

    void modify(Merchant resources);

    void delete(Set<Long> ids);

    void create( Merchant resources);

    void audit(Merchant resources);

    PageResult<RegionDto> getRegion(String value);
    PageResult<Region2Dto> getRegion();
}
