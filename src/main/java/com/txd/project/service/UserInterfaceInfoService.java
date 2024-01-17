package com.txd.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.txd.project.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.txd.project.model.entity.InterfaceInfo;
import com.txd.project.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author tangx
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-12-02 20:36:50
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean b);

    public boolean invokeCount(long interfaceInfoId, long userId);
}
