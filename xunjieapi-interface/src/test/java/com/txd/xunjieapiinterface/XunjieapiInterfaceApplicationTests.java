package com.txd.xunjieapiinterface;

import com.txd.xunjieapiclientsdk.client.XJApiClient;
import com.txd.xunjieapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class XunjieapiInterfaceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private XJApiClient xjApiClient;

    @Test
    public void testXJApiClient() {
        System.out.println(xjApiClient.getNameByGet("小石头"));
        System.out.println(xjApiClient.getNameByPost("重瞳者"));
        User user = new User();
        user.setUserName("shihao");
        System.out.println(xjApiClient.getNameByUser(user));
    }

}
