package com.txd.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.txd.common.core.model.entity.UserInterfaceInfo;

/**
* @author tangx
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-12-02 20:36:50
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean b);

    boolean invokeCount(long interfaceInfoId, long userId);


}
