package me.zhengjie.rest;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.config.SmsConfig;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.utils.RedisUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sms")
@Api(tags = "工具：短信发送")
public class SmsController {
    private final RedisUtils redisUtils;
    @GetMapping(value = "/send")
    @AnonymousAccess
    public ResponseEntity<Object> sendSms(String to) {
        try {
            SmsConfig.initTwilio();
            //生成四位验证码随机数
            int code=(int)(Math.random()*9+1)*1000;
            String message = "【吉象同城】您的验证码是：" +code;
            Message.creator(new PhoneNumber(to), new PhoneNumber(SmsConfig.TWILIO_PHONE_NUMBER), message)
                    .create();
            //保存验证码五分钟
            redisUtils.set(to, code, 5L, TimeUnit.MINUTES);
        }catch (Exception e){
            e.printStackTrace();
            throw new BadRequestException("短信发送失败");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
