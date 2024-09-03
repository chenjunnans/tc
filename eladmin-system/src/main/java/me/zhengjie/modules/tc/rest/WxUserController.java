package me.zhengjie.modules.tc.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.modules.tc.domain.WxUser;
import me.zhengjie.modules.tc.service.WxUserService;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Api(tags = "微信用户")
public class WxUserController {
    private  final WxUserService wxUserService ;
    @ApiOperation("查询微信用户")
    @GetMapping
    @PreAuthorize("@el.check('wx_user:list')")
    public ResponseEntity<PageResult<WxUser>> queryAll(Pageable pageable) {
        return new ResponseEntity<>( wxUserService.queryAll( pageable), HttpStatus.OK);
    }


    @ApiOperation("登录微信用户")
    @PostMapping("/login")
    @AnonymousAccess
    public ResponseEntity<Object> login(String code,String username,String face ) {
        return new ResponseEntity<>( wxUserService.login( code, username,face), HttpStatus.OK);
    }
    @ApiOperation("修改微信用户")
    @PostMapping("/modify")
    public ResponseEntity<Object> modify(@RequestBody WxUser wxUser) {
        wxUserService.modify( wxUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation("手机登录")
    @PostMapping("/login_phone")
    @AnonymousAccess
    public ResponseEntity<Object> loginPhone(String code,String username) {
        return new ResponseEntity<>( wxUserService.loginPhone( code, username), HttpStatus.OK);
    }
}
