package com.txd.project.utils;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import darabonba.core.client.ClientOverrideConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class MessageTemplate {
 
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.region}")
    private String region;
    @Value("${aliyun.signName}")
    private String signName;
    @Value("${aliyun.templateCode}")
    private String templateCode;


    //提供发送短信验证码的方法，参数为：手机号 ， 验证码
    public void doSendSMSCode(String phone,String code){
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKeyId)
                .accessKeySecret(accessKeySecret)
                .build());

        AsyncClient client = AsyncClient.builder()
                .region(region) // Region ID
                .credentialsProvider(provider)
                .overrideConfiguration(ClientOverrideConfiguration.create().setEndpointOverride("dysmsapi.aliyuncs.com"))
                .build();

        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName(signName)
                .templateCode(templateCode)
                .phoneNumbers(phone)
                .templateParam("{\"code\":\""+code+"\"}")
                .build();
        //执行发送
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        SendSmsResponse resp = null;
        try {
            resp = response.get();
        } catch (Exception e) {
            e.printStackTrace();
            //出现错误
//            Map<String,Object> result = new HashMap<>();
//            result.put("error",e.getMessage());
//            return result;
        }

        //处理结果
//        String json = new Gson().toJson(resp);
//        Map<String,Object> result = JSON.parseObject(json,Map.class);
        client.close();
//        return result;
    }
}