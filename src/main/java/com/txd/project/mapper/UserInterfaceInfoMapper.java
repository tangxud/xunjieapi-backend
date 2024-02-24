package com.txd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.txd.common.core.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author tangx
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2023-12-02 20:36:50
* @Entity com.txd.project.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    /**
     * 查询top limit的接口集合
     * @param limit
     * @return
     */
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




