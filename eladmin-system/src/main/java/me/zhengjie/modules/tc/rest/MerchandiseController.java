package me.zhengjie.modules.tc.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.tc.domain.Merchandise;
import me.zhengjie.modules.tc.service.MerchandiseService;
import me.zhengjie.modules.tc.service.dto.MerchandiseQueryCriteria;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/merchandise")
@Api(tags = "商品消息管理")
public class MerchandiseController {

    private final MerchandiseService merchandiseService;
    @ApiOperation("查询商品消息")
    @GetMapping
    @PreAuthorize("@el.check('merchandise:list')")
    public ResponseEntity<PageResult<Merchandise>> queryAll(MerchandiseQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>( merchandiseService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @ApiOperation("查询商品消息--小程序")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<PageResult<Merchandise>> queryAll2(MerchandiseQueryCriteria criteria,  Pageable pageable){
        if(criteria.getStatus()==null){
            criteria.setStatus(1);
        }
        return new ResponseEntity<>( merchandiseService.queryAll2(criteria,pageable), HttpStatus.OK);
    }
    @ApiOperation("查询商品消息--小程序")
    @PostMapping("{id}")
    @AnonymousAccess
    public ResponseEntity<Merchandise> query(@PathVariable Long id){
        return new ResponseEntity<>( merchandiseService.query(id), HttpStatus.OK);
    }
    @Log("新增商品消息")
    @ApiOperation("新增商品消息")
    @PostMapping("add")
    @PreAuthorize("@el.check('merchandise:add')")
    public ResponseEntity<Object> createDept(@Validated @RequestBody Merchandise resources){
        if (resources.getId() != null) {
            throw new BadRequestException("新增商品消息失败！");
        }

        merchandiseService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Log("新增商品消息")
    @ApiOperation("新增商品消息")
    @PostMapping("add2")
    @AnonymousAccess
    public ResponseEntity<Object> add2(@Validated @RequestBody Merchandise resources){
        if(StringUtils.isBlank(resources.getOpenId())){
            throw new BadRequestException("用户登录失效！");
        }
        merchandiseService.create2(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Log("修改商品消息")
    @ApiOperation("修改商品消息")
    @PostMapping("modify")
    @PreAuthorize("@el.check('merchandise:modify')")
    public ResponseEntity<Object> modify(@Validated @RequestBody Merchandise resources){
        if(resources.getId()==null){
            throw new BadRequestException("修改商品信息有误");
        }
        merchandiseService.modify(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Log("删除商品消息")
    @ApiOperation("删除商品消息")
    @PostMapping("delete")
    @PreAuthorize("@el.check('merchandise:delete')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
        merchandiseService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("删除商品消息")
    @ApiOperation("删除商品消息")
    @PostMapping("delete2")
    @AnonymousAccess
    public ResponseEntity<Object> delete2(Long id) {
        merchandiseService.delete2(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
