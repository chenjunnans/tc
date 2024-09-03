package me.zhengjie.modules.tc.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.tc.domain.Merchandise;
import me.zhengjie.modules.tc.domain.Merchant;
import me.zhengjie.modules.tc.service.MerchantService;
import me.zhengjie.modules.tc.service.dto.MerchandiseQueryCriteria;
import me.zhengjie.modules.tc.service.dto.Region2Dto;
import me.zhengjie.modules.tc.service.dto.RegionDto;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/merchant")
@Api(tags = "入驻商家管理")

public class MerchantController {
    private final MerchantService merchantService;
    @ApiOperation("查询入驻商家")
    @GetMapping
    @PreAuthorize("@el.check('merchant:list')")
    public ResponseEntity<PageResult<Merchant>> queryAll(MerchandiseQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>( merchantService.queryAll(criteria, pageable), HttpStatus.OK);
    }
    @ApiOperation("新增入驻商家--注冊")
    @PostMapping("add")
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody Merchant resources){
        if (resources.getId() != null) {
            throw new BadRequestException("新增入驻商家！");
        }
        merchantService.create(resources);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("入驻商家--审核")
    @PostMapping("audit")
    @PreAuthorize("@el.check('merchant:modify')")
    public ResponseEntity<Object> audit(@RequestBody Merchant resources){
        if (resources.getId() == null) {
            throw new BadRequestException("该商家信息不存在！");
        }
        merchantService.audit(resources);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("修改入驻商家")
    @ApiOperation("修改入驻商家")
    @PostMapping("modify")
    @PreAuthorize("@el.check('merchant:modify')")
    public ResponseEntity<Object> modify(@Validated @RequestBody Merchant resources){
        if(resources.getId()==null){
            throw new BadRequestException("入驻商家修改失败");
        }
        merchantService.modify(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Log("删除入驻商家")
    @ApiOperation("删除入驻商家")
    @PostMapping("delete")
    @PreAuthorize("@el.check('merchant:delete')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
        merchantService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "省市区三级联动", httpMethod = "GET", response = ModelMap.class)
    @GetMapping("/getRegion/{value:.+}")
    @AnonymousAccess
    public  PageResult<RegionDto> getRegion(@PathVariable("value") String value) {
        return merchantService.getRegion(value);
    }

    @ApiOperation(value = "省市区三级联动", httpMethod = "GET", response = ModelMap.class)
    @GetMapping("/getRegion")
    @AnonymousAccess
    public  PageResult<Region2Dto> getRegion() {
        return merchantService.getRegion();
    }

}
