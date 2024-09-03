package me.zhengjie.config;

import com.twilio.Twilio;

public class SmsConfig {
    public static  String accountSid = "***";
    public static  String authToken = "***";
    public static  String  TWILIO_PHONE_NUMBER="***";

    public static void initTwilio() {
        Twilio.init(accountSid, authToken);
    }
}
