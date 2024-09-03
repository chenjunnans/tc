package me.zhengjie.modules.tc.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.tc.domain.WxUser;
import me.zhengjie.modules.tc.repository.WxUserRepository;
import me.zhengjie.modules.tc.service.WxUserService;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.RedisUtils;
import me.zhengjie.utils.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WxUserServiceImpl implements WxUserService {
    private static final String GET_OPENID_ACCESSTOKEN = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    private static final String GET_USERINFO = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
    private final String APP_ID = "wxde9f638ccc647804";
    private final String SECRET = "8137e3a1578812881116895142a25e7c";
    private final WxUserRepository wxUserRepository;
    private final RedisUtils redisUtils;

    @Override
    public PageResult<WxUser> queryAll(Pageable pageable) {
        return PageUtil.toPage(wxUserRepository.findAll(pageable));
    }

    @Override
    public Object login(String code, String username, String face) {
        String requestUrl = String.format(GET_OPENID_ACCESSTOKEN, APP_ID, SECRET, code);
        String result = HttpUtil.get(requestUrl);
        JSONObject entries = new JSONObject(result);
        Map<String,String> map=new HashMap<>();
        map.put("username", username);
        map.put("face", face);
        map.put("signature", "已登录");
        map.put("openid", entries.getStr("openid"));
        map.put("session_key", entries.getStr("session_key"));
        WxUser wxUser = wxUserRepository.findByOpenid(entries.getStr("openid")).orElse(new WxUser());
        wxUser.setOpenid(entries.getStr("openid"));
        wxUser.setUsername(username);
        wxUser.setFace(face);
        wxUser.setTime(DateUtil.now());
        wxUser.setNumber(3);
        wxUserRepository.save(wxUser);
        return map;

    }

    @Override
    public void modify(WxUser wxUser) {
        if(wxUser.getId()!=null){
            wxUserRepository.save(wxUser);
        }
    }

    @Override
    public Object loginPhone(String code, String username) {
        // 查询验证码
        String code2 = (String) redisUtils.get(username);
        // 清除验证码
        redisUtils.del(username);
        if (StringUtils.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(code) || !code.equalsIgnoreCase(code2)) {
            throw new BadRequestException("验证码错误");
        }
        Map<String,String> map=new HashMap<>();
        map.put("username", username);
        map.put("face", "");
        map.put("signature", "已登录");
        map.put("openid",username );
        WxUser wxUser = wxUserRepository.findByOpenid(username).orElse(new WxUser());
        wxUser.setOpenid(username);
        wxUser.setUsername(username);
        wxUser.setTime(DateUtil.now());
        wxUser.setNumber(3);
        wxUserRepository.save(wxUser);
        return map;
    }

    public JSONObject getUserInfo(String accessToken, String openid) {
        String requestUrl = String.format(GET_USERINFO, accessToken, openid);
        try {
            String result = HttpUtil.get(requestUrl);
            JSONObject json = new JSONObject(result);
            if (json.getStr("openid") != null) {
                return json;
            } else {
                throw new BadRequestException("获取用户信息失败,错误原因:" + json.getStr("errcode") + "," + json.getStr("errmsg"));
            }
        } catch (Exception e) {
            throw new BadRequestException("获取用户信息失败,连接微信服务器故障," + e.getMessage());

        }

    }
}
