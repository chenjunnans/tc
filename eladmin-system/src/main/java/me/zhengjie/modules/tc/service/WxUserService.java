package me.zhengjie.modules.tc.service;

import me.zhengjie.modules.tc.domain.WxUser;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;

public interface WxUserService {
     PageResult<WxUser> queryAll(Pageable pageable);

     Object login(String code, String name, String url);

     void  modify(WxUser wxUser);

     Object loginPhone(String code, String username);
}
